package nz.co.thewarehouse.productsearch.data

interface ProductRepository {

    suspend fun searchProducts(query: String): Result<List<Product>>
}