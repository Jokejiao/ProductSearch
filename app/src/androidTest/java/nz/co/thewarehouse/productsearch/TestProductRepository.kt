package nz.co.thewarehouse.productsearch

import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import nz.co.thewarehouse.productsearch.data.toExternal
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TestProductRepository @Inject constructor() : ProductRepository {

    private val fakeProducts = listOf(
        NetworkProduct(productId = "1", productName = "Laptop 1"),
        NetworkProduct(productId = "2", productName = "Laptop 2")
    )

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return Result.success(fakeProducts.toExternal())
    }
}
