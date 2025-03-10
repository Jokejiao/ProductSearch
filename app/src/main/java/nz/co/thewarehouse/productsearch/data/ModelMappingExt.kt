package nz.co.thewarehouse.productsearch.data

import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import kotlin.collections.map


// Network to External
fun NetworkProduct.toExternal() = Product(
    productId = productId,
    productName = productName,
    price = priceInfo?.price ?: 0.0,
    productImageUrl = productImageUrl,
    productDescription = productDescription ?: "Description not available",
    promotions = promotions.mapNotNull { it.dealDescription },
    available = inventory?.available == true,
    imageUrls = imageGroups.flatMap { it.imageUrls }
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("networkToExternal")
fun List<NetworkProduct>.toExternal() = map(NetworkProduct::toExternal)