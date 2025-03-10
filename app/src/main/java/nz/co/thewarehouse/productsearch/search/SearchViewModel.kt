package nz.co.thewarehouse.productsearch.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.util.WhileUiSubscribed
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
        initialValue = ProductsUiState(isSearching = false)
    )

    fun onSearchTriggered(query: String) {
        if (query.isBlank()) return

        _isSearching.value = true

        viewModelScope.launch {
            try {
                val products = repository.searchProducts(query)
                _products.value = products
                _userMessage.value = null
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error searching products: ${e.message}")
                _userMessage.value = R.string.search_products_error
            } finally {
                _isSearching.value = false
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
}

data class NoProductsUiInfo(
    val noProductsLabel: Int = R.string.no_products,
    val noProductsIconRes: Int = R.drawable.logo_no_fill,
)

private const val SEARCH_QUERY = "searchQuery"