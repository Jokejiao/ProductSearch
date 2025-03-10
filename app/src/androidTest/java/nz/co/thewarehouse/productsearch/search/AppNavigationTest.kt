package nz.co.thewarehouse.productsearch.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import nz.co.thewarehouse.productsearch.HiltTestActivity
import nz.co.thewarehouse.productsearch.ProductSearchNavGraph
import nz.co.thewarehouse.productsearch.ProductSearchTheme
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.data.ProductRepository
import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


/**
 * Tests for scenarios that requires navigating within the app.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AppNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Executes products in the Architecture Components in the same thread
    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var productRepository: ProductRepository

    private val fakeProducts = listOf(
        NetworkProduct(productId = "1", productName = "Laptop 1"),
        NetworkProduct(productId = "2", productName = "Laptop 2")
    )

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun productScreen_backButton() = runTest {
        setContent()

        composeTestRule.waitForIdle()

        // Click on the product on the list
        composeTestRule.onNodeWithText("Laptop 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Laptop 1").performClick()

        // The Product screen is shown
        composeTestRule.onNodeWithText("Product Details").assertIsDisplayed()

        // Confirm that if we click "<-" once, we end up back at the product list page
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_back))
            .performClick()
        composeTestRule.onNodeWithText("ProductSearch").assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            ProductSearchTheme {
                val viewModel: SearchViewModel = hiltViewModel()
                ProductSearchNavGraph(viewModel = viewModel)

                viewModel.onSearchQueryChanged("Laptop")
                viewModel.onSearchTriggered("Laptop")
            }
        }
    }
}
