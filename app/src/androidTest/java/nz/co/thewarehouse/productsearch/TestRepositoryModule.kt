package nz.co.thewarehouse.productsearch

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.di.RepositoryModule
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository {
        return TestProductRepository()
    }
}