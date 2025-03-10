package nz.co.thewarehouse.productsearch.search

import androidx.lifecycle.SavedStateHandle
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import nz.co.thewarehouse.productsearch.data.toExternal
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    // Use UnconfinedTestDispatcher for immediate execution
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mock dependencies
    private val mockRepository: ProductRepository = mockk()
    private lateinit var savedStateHandle: SavedStateHandle

    // ViewModel under test
    private lateinit var viewModel: SearchViewModel

    private val fakeProducts = listOf(
        NetworkProduct(productId = "1", productName = "Laptop 1"),
        NetworkProduct(productId = "2", productName = "Laptop 2")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)  // Set Main dispatcher for test

        savedStateHandle = SavedStateHandle(mapOf("searchQuery" to ""))
        viewModel = SearchViewModel(savedStateHandle, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Cleanup
        clearAllMocks()
    }

    @Test
    fun checkInitialState() = runTest {
        val state = viewModel.uiState.first()

        assertEquals(emptyList<Product>(), state.items)
        assertFalse(state.isSearching)
        assertNull(state.userMessage)
    }

    @Test
    fun onSearchTriggered_updatesProducts_clearsUserMessage() = runTest {
        coEvery { mockRepository.searchProducts("Laptop") } returns Result.success(fakeProducts.toExternal())

        viewModel.onSearchTriggered("Laptop")

        val state = viewModel.uiState.first()
        assertEquals(fakeProducts.toExternal(), state.items)
        assertFalse(state.isSearching)
        assertNull(state.userMessage)

        coVerify(exactly = 1) { mockRepository.searchProducts("Laptop") }
    }

    @Test
    fun onSearchTriggered_setsErrorUserMessage() = runTest {
        coEvery { mockRepository.searchProducts("Laptop") } returns Result.failure(
            RuntimeException(
                "Network error"
            )
        )

        viewModel.onSearchTriggered("Laptop")

        val state = viewModel.uiState.first()
        assertEquals(R.string.search_products_error, state.userMessage)
        assertFalse(state.isSearching)

        coVerify(exactly = 1) { mockRepository.searchProducts("Laptop") }
    }

    @Test
    fun onSearchQueryChanged_updatesSavedStateHandle() {
        viewModel.onSearchQueryChanged("New Query")
        assertEquals("New Query", savedStateHandle["searchQuery"])
    }

    @Test
    fun snackbarMessageShown_clearsUserMessage() = runTest {
        coEvery { mockRepository.searchProducts("Laptop") } returns Result.success(fakeProducts.toExternal())

        viewModel.onSearchTriggered("Laptop")
        viewModel.snackbarMessageShown()

        val state = viewModel.uiState.first()
        assertNull(state.userMessage)
    }

    @Test
    fun getProductById_returnsCorrectProduct() = runTest {
        coEvery { mockRepository.searchProducts("Laptop") } returns Result.success(fakeProducts.toExternal())

        viewModel.onSearchTriggered("Laptop")

        val product = viewModel.getProductById("1")
        assertNotNull(product)
        assertEquals("Laptop 1", product?.productName)
    }

    @Test
    fun getProductById_returnsNullForMissingProduct() {
        val product = viewModel.getProductById("999") // Non-existent product
        assertNull(product)
    }
}
