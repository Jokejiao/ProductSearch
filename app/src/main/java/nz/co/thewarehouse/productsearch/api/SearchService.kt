package nz.co.thewarehouse.productsearch.api

import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("twgCSharpTest/Search.json")
    suspend fun searchProducts(
        @Query("Search") query: String,
        @Query("Limit") limit: Int,

    ): List<NetworkProduct>
}