package co.jonathanbernal.comerzi.datasource.network.models

data class FireStoreCategoryResponse(
    val idCategory: String,
    val name: String
)

data class FireStoreCategory(
    val id: String? = null,
    val name: String
)