package co.jonathanbernal.comerzi.datasource.network.models

data class FireStoreProduct(
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    val idCategory: String,
)

data class FireStoreProductResponse(
    val id: String,
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    val idCategory: String
)