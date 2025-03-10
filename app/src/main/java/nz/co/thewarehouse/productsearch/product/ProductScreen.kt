package nz.co.thewarehouse.productsearch.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import nz.co.thewarehouse.productsearch.data.Product
import nz.co.thewarehouse.productsearch.R
import nz.co.thewarehouse.productsearch.search.SearchViewModel
import nz.co.thewarehouse.productsearch.util.HtmlText
import nz.co.thewarehouse.productsearch.util.ProductTopAppBar


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    productId: String,
    viewModel: SearchViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProductTopAppBar(
                onBack
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ProductContent(
            product = viewModel.getProductById(productId),
            modifier = Modifier.padding(paddingValues)
        )

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
fun ProductContent(
    product: Product?,
    modifier: Modifier = Modifier
) {
    if (product != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                ProductImageCarousel(product.imageUrls)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.productName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.product_price, product.price),
                    fontSize = 20.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                HtmlText(
                    html = product.productDescription.toString(),
                )

                Spacer(modifier = Modifier.height(8.dp))

                AvailabilityChip(isAvailable = product.available)

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                PromotionSection(promotions = product.promotions)
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.product_not_found),
                fontSize = 18.sp,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageCarousel(imageUrls: List<String>) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = stringResource(R.string.product_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageUrls.size) { index ->
                val color = if (pagerState.currentPage == index) Color.Black else Color.LightGray
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(2.dp)
                        .background(color, CircleShape)
                )
            }
        }
    }
}

@Composable
fun AvailabilityChip(isAvailable: Boolean) {
    val backgroundColor = if (isAvailable) Color.Green else Color.Red
    val text =
        if (isAvailable) stringResource(R.string.product_available) else stringResource(R.string.product_out_of_stock)

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PromotionSection(promotions: List<String>) {
    if (promotions.isNotEmpty()) {
        Column {
            Text(
                text = stringResource(R.string.promotions),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            promotions.forEach { promo ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color(0xFFFFF3E0))
                ) {
                    Text(
                        text = promo,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
