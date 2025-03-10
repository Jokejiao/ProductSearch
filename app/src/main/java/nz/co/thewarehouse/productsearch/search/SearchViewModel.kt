package nz.co.thewarehouse.productsearch.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.util.WhileUiSubscribed
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


data class ProductsUiState(
    val items: List<Product> = emptyList(),
    val isSearching: Boolean = false,
    val noProductsUiInfo: NoProductsUiInfo = NoProductsUiInfo(),
    val userMessage: Int? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository
) : ViewModel() {

    private var searchJob: Job? = null
    private var jobCount = AtomicInteger(0)
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    private val _isSearching = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    val uiState: StateFlow<ProductsUiState> = combine(
        _products,
        _isSearching,
        _userMessage
    ) { products, isSearching, userMessage ->
        ProductsUiState(
            items = products,
            isSearching = isSearching,
            userMessage = userMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = ProductsUiState()
    )

    fun onSearchTriggered(query: String) {
        if (query.isBlank()) return

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _isSearching.value = true
            jobCount.incrementAndGet()

            try {
                repository.searchProducts(query)
                    .onSuccess { products ->
                        _products.value = products
                        _userMessage.value = null
                    }
                    .onFailure { e ->
                        showSnackbarMessage(R.string.search_products_error)
                    }
            } finally {
                if (jobCount.decrementAndGet() == 0) {
                    _isSearching.value = false
                }
            }
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun getProductById(productId: String): Product? {
        return _products.value.find { it.productId == productId }
    }
}

data class NoProductsUiInfo(
    val noProductsLabel: Int = R.string.no_products,
    val noProductsIconRes: Int = R.drawable.logo_no_fill,
)

private const val SEARCH_QUERY = "searchQuery"