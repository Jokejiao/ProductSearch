package nz.co.thewarehouse.productsearch.data

data class UiProduct(
    val productName: String,
    val price: Double,
    val productImageUrl: String,
    val productDescription: String,
    val dealDescription: List<String>,
)