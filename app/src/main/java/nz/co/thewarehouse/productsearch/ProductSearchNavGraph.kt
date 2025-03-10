package nz.co.thewarehouse.productsearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.co.thewarehouse.productsearch.search.SearchScreen


@Composable
fun ProductSearchNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ProductSearchDestinations.SEARCH_ROUTE,
    navActions: ProductSearchNavigationActions = remember(navController) {
        ProductSearchNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ProductSearchDestinations.SEARCH_ROUTE) {
            SearchScreen(
                onProductClick = { product ->
                    navActions.navigateToProduct(
                        product.productId
                    )
                }
            )
        }
        composable(ProductSearchDestinations.PRODUCT_ROUTE) {
//            ProductScreen(
//                onBack = { navController.popBackStack() },
//            )
        }
    }
}