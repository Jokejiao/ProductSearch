package nz.co.thewarehouse.productsearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.co.thewarehouse.productsearch.product.ProductScreen
import nz.co.thewarehouse.productsearch.search.SearchScreen
import nz.co.thewarehouse.productsearch.search.SearchViewModel


@Composable
fun ProductSearchNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ProductSearchDestinations.SEARCH_ROUTE,
    navActions: ProductSearchNavigationActions = remember(navController) {
        ProductSearchNavigationActions(navController)
    },
    viewModel: SearchViewModel = hiltViewModel()
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ProductSearchDestinations.SEARCH_ROUTE) {
            SearchScreen(
                viewModel = viewModel,
                onProductClick = { product ->
                    navActions.navigateToProduct(
                        product.productId
                    )
                }
            )
        }
        composable(ProductSearchDestinations.PRODUCT_ROUTE,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductScreen(
                viewModel = viewModel,
                productId = productId ?: "",
                onBack = { navController.popBackStack() },
            )
        }
    }
}