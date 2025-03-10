package nz.co.thewarehouse.productsearch.data

data class Product(
    val productId: String,
    val productName: String,
    val price: Double,
    val productImageUrl: String,
    val productDescription: String,
    val dealDescription: List<String>,
)