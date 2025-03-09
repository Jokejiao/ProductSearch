package nz.co.thewarehouse.productsearch.data.source.network

interface NetworkDataSource {

    suspend fun searchProducts(query: String): List<NetworkProduct>
}