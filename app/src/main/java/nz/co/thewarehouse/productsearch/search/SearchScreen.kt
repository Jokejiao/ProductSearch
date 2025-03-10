package nz.co.thewarehouse.productsearch.search

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.util.LoadingContent
import nz.co.thewarehouse.productsearch.util.SearchTextField
import nz.co.thewarehouse.productsearch.util.SearchTopAppBar


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    searchQuery: String = viewModel.searchQuery.collectAsStateWithLifecycle().value,
    onSearchQueryChanged: (String) -> Unit = viewModel::onSearchQueryChanged,
    onSearchTriggered: (String) -> Unit = viewModel::onSearchTriggered,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SearchTopAppBar()
        },
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = onSearchTriggered
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProductsContent(
                searching = uiState.isSearching,
                products = uiState.items,
                noProductsLabel = uiState.noProductsUiInfo.noProductsLabel,
                noProductsIconRes = uiState.noProductsUiInfo.noProductsIconRes,
                onProductClick = onProductClick,
                modifier = Modifier.weight(1f)
            )
        }

        // Check for user messages to display on the screen
        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
private fun ProductsContent(
    searching: Boolean,
    products: List<Product>,
    @StringRes noProductsLabel: Int,
    @DrawableRes noProductsIconRes: Int,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = searching,
        empty = products.isEmpty() && !searching,
        emptyContent = {
            ProductsEmptyContent(
                noProductsLabel,
                noProductsIconRes,
                modifier
            )
        },
        onRefresh = {}
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            LazyColumn {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onProductClick = onProductClick,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onProductClick(product) }
            .semantics {
                contentDescription = product.productName
                onClick { true }
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.productImageUrl,
                placeholder = painterResource(R.drawable.product_placeholder),
                error = painterResource(R.drawable.product_placeholder),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun ProductsEmptyContent(
    @StringRes noProductsLabel: Int,
    @DrawableRes noProductsIconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noProductsIconRes),
            contentDescription = stringResource(R.string.no_products_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = noProductsLabel))
    }
}