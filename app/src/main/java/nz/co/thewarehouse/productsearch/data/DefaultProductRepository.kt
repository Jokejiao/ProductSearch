package nz.co.thewarehouse.productsearch.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import nz.co.thewarehouse.productsearch.data.source.network.NetworkDataSource
import nz.co.thewarehouse.productsearch.di.IoDispatcher
import javax.inject.Inject


class DefaultProductRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ProductRepository {

    override suspend fun searchProducts(query: String): List<Product> =
        withContext(dispatcher) {
            runCatching {
                networkDataSource.searchProducts(query).toExternal()
            }.onFailure { e ->
                Log.e("Repository", "Search products failed: ${e.message}")
            }.getOrElse {
                emptyList()
            }
        }
}