package nz.co.thewarehouse.productsearch

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import nz.co.thewarehouse.productsearch.ProductSearchDestinationsArgs.PRODUCT_ID_ARG
import nz.co.thewarehouse.productsearch.ProductSearchScreens.PRODUCT_SCREEN
import nz.co.thewarehouse.productsearch.ProductSearchScreens.SEARCH_SCREEN


/**
 * Screens used in [ProductSearchDestinations]
 */
private object ProductSearchScreens {
    const val SEARCH_SCREEN = "search"
    const val PRODUCT_SCREEN = "product"
}

/**
 * Arguments used in [ProductSearchDestinations] routes
 */
object ProductSearchDestinationsArgs {
    const val PRODUCT_ID_ARG = "productId"
}

/**
 * Destinations used in the [ProductSearchActivity]
 */
object ProductSearchDestinations {
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val PRODUCT_ROUTE = "$PRODUCT_SCREEN/{$PRODUCT_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class ProductSearchNavigationActions(private val navController: NavHostController) {

    fun navigateToSearch() {
        navController.navigate(SEARCH_SCREEN) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    fun navigateToProduct(productId: String) {
        navController.navigate("$PRODUCT_SCREEN/$productId")
    }
}
