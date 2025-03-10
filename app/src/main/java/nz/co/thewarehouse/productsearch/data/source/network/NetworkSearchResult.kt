package nz.co.thewarehouse.productsearch.data.source.network

data class SearchResult(
    val products: List<NetworkProduct> = emptyList()
)

data class NetworkProduct(
    val brandCode: String? = null,
    val brandDescription: String? = null,
    val categoryId: String? = null,
    val clickAndCollect: String? = null,
    val colourAttribute: String? = null,
    val colourDescription: String? = null,
    val deliveryTime: String? = null,
    val imageGroups: List<ImageGroup> = emptyList(),
    val inventory: Inventory? = null,
    val isClickAndCollect: Boolean = false,
    val isDigital: Boolean = false,
    val isEssentialItem: Boolean = false,
    val isGiftcard: Boolean = false,
    val isMarketPlace: Boolean = false,
    val isOversized: Boolean = false,
    val masterColourCode: String? = null,
    val masterProductId: String? = null,
    val mdmProductId: Any? = null,
    val priceInfo: PriceInfo? = null,
    val productBadges: List<ProductBadge> = emptyList(),
    val productBarcode: String? = null,
    val productDescription: String? = null,
    val productId: String = "",
    val productImageUrl: String? = null,
    val productName: String = "",
    val productSet: Boolean = false,
    val promotions: List<Promotion> = emptyList(),
    val shippingSize: String? = null,
    val soldOnline: String? = null,
    val subClassId: String? = null,
    val variantGroupId: String? = null,
    val variants: List<Variant> = emptyList(),
    val vendorCountryCode: String? = null,
    val vendorCountryName: String? = null,
    val vendorName: String? = null
)

data class ImageGroup(
    val colourAttribute: String? = null,
    val imageUrls: List<String> = emptyList()
)

data class Inventory(
    val available: Boolean = false,
    val backorderable: Boolean = false,
    val preorderable: Boolean = false,
    val soh: Int = 0
)

data class PriceInfo(
    val price: Double = 0.0
)

data class ProductBadge(
    val definition: Definition? = null,
    val id: String? = null
)

data class Promotion(
    val dealDescription: String? = null,
    val description: String? = null,
    val isMarketClubExclusive: Boolean = false,
    val promotionId: String? = null,
    val tags: List<String> = emptyList()
)

data class Variant(
    val colourAttribute: String? = null,
    val optionValue: Double? = null,
    val orderable: Boolean = false,
    val price: Double = 0.0,
    val productId: String? = null,
    val sizeAttribute: String? = null,
    val sizeDescription: String? = null
)

data class Definition(
    val backgroundColour: String? = null,
    val description: String? = null,
    val foregroundColour: String? = null,
    val order: Int = 0
)
