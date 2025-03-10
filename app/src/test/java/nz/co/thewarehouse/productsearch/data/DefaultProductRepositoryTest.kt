package nz.co.thewarehouse.productsearch.data

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import nz.co.thewarehouse.productsearch.data.source.network.NetworkDataSource
import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class DefaultProductRepositoryTest {

    // Mock dependencies
    private val mockNetworkDataSource: NetworkDataSource = mockk()

    // Use a TestCoroutineDispatcher to control coroutine execution
    private val testDispatcher = UnconfinedTestDispatcher()

    // Repository under test
    private lateinit var repository: DefaultProductRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Initialize repository with mocks and test dispatcher
        repository = DefaultProductRepository(mockNetworkDataSource, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun searchProducts_returnAllProducts() = runTest {
        // Given
        val fakeProducts = listOf(
            NetworkProduct(productId = "1", productName = "Laptop 1"),
            NetworkProduct(productId = "2", productName = "Laptop 2")
        )
        coEvery { mockNetworkDataSource.searchProducts("Laptop") } returns fakeProducts

        // When
        val result = repository.searchProducts("Laptop")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(fakeProducts.toExternal(), result.getOrNull())

        // Verify that the network call was made once
        coVerify(exactly = 1) { mockNetworkDataSource.searchProducts("Laptop") }
    }

    @Test
    fun searchProducts_returnError() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { mockNetworkDataSource.searchProducts("Laptop") } throws exception

        // When
        val result = repository.searchProducts("Laptop")

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        // Verify that the network call was made once
        coVerify(exactly = 1) { mockNetworkDataSource.searchProducts("Laptop") }
    }

    @Test
    fun searchProducts_returnEmptyList() = runTest {
        // Given
        coEvery { mockNetworkDataSource.searchProducts("unknown") } returns emptyList()

        // When
        val result = repository.searchProducts("unknown")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList<Product>(), result.getOrNull())

        // Verify that the network call was made once
        coVerify(exactly = 1) { mockNetworkDataSource.searchProducts("unknown") }
    }
}
