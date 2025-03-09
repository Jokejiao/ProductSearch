package nz.co.thewarehouse.productsearch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.thewarehouse.productsearch.data.DefaultProductRepository
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.data.source.network.NetworkDataSource
import nz.co.thewarehouse.productsearch.data.source.network.ProductNetworkDataSource
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTransactionRepository(repository: DefaultProductRepository): ProductRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: ProductNetworkDataSource): NetworkDataSource
}