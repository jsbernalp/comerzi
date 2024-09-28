package co.jonathanbernal.comerzi.datasource.network.models

data class FireStoreProduct(
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    val category: FireStoreCategory,
)

data class FireStoreProductResponse(
    val id: String,
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    val category: Map<String, String>,
)