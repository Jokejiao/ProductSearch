package nz.co.thewarehouse.productsearch.data.source.network

import nz.co.thewarehouse.productsearch.api.SearchService
import javax.inject.Inject


class ProductNetworkDataSource @Inject constructor(
    private val searchService: SearchService
) : NetworkDataSource {

    override suspend fun searchProducts(query: String): List<NetworkProduct> =
        searchService.searchProducts(query, 10).products
}