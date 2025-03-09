package nz.co.thewarehouse.productsearch.data

import nz.co.thewarehouse.productsearch.data.source.network.NetworkProduct
import kotlin.collections.map


// Network to External
fun NetworkProduct.toExternal() = UiProduct(
    productName = productName,
    price = priceInfo.price,
    productImageUrl = productImageUrl,
    productDescription = productDescription,
    dealDescription = promotions.map { it.dealDescription }
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("networkToExternal")
fun List<NetworkProduct>.toExternal() = map(NetworkProduct::toExternal)