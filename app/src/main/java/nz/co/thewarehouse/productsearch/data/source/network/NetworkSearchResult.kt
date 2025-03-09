package nz.co.thewarehouse.productsearch.data.source.network


data class SearchResult(
    val products: List<NetworkProduct>,
)

data class NetworkProduct(
    val brandCode: String,
    val brandDescription: String,
    val categoryId: String,
    val clickAndCollect: String,
    val colourAttribute: String,
    val colourDescription: String,
    val deliveryTime: String,
    val imageGroups: List<ImageGroup>,
    val inventory: Inventory,
    val isClickAndCollect: Boolean,
    val isDigital: Boolean,
    val isEssentialItem: Boolean,
    val isGiftcard: Boolean,
    val isMarketPlace: Boolean,
    val isOversized: Boolean,
    val masterColourCode: String,
    val masterProductId: String,
    val mdmProductId: Any,
    val priceInfo: PriceInfo,
    val productBadges: List<ProductBadge>,
    val productBarcode: String,
    val productDescription: String,
    val productId: String,
    val productImageUrl: String,
    val productName: String,
    val productSet: Boolean,
    val promotions: List<Promotion>,
    val shippingSize: String,
    val soldOnline: String,
    val subClassId: String,
    val variantGroupId: String,
    val variants: List<Variant>,
    val vendorCountryCode: String,
    val vendorCountryName: String,
    val vendorName: String
)

data class ImageGroup(
    val colourAttribute: String,
    val imageUrls: List<String>
)

data class Inventory(
    val available: Boolean,
    val backorderable: Boolean,
    val preorderable: Boolean,
    val soh: Int
)

data class PriceInfo(
    val price: Double
)

data class ProductBadge(
    val definition: Definition,
    val id: String
)

data class Promotion(
    val dealDescription: String,
    val description: String,
    val isMarketClubExclusive: Boolean,
    val promotionId: String,
    val tags: List<String>
)

data class Variant(
    val colourAttribute: String,
    val optionValue: Double,
    val orderable: Boolean,
    val price: Double,
    val productId: String,
    val sizeAttribute: String,
    val sizeDescription: String
)

data class Definition(
    val backgroundColour: String,
    val description: String,
    val foregroundColour: String,
    val order: Int
)