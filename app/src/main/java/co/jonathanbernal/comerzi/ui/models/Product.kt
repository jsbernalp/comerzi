package co.jonathanbernal.comerzi.ui.models

data class Product(
    val name: String = "",
    val ean: String = "",
    val price: Double = 0.0,
    val category: Category
)