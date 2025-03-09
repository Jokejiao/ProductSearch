package nz.co.thewarehouse.productsearch.data.source.network

import javax.inject.Inject

class ProductNetworkDataSource @Inject constructor(
) : NetworkDataSource {

    override suspend fun searchProducts(query: String): List<NetworkProduct> {
        return emptyList()
    }
}