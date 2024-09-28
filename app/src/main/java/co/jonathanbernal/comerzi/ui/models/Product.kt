package co.jonathanbernal.comerzi.ui.models

data class Product(
    val idProduct: String = "",
    val name: String = "",
    val ean: String = "",
    val price: Double = 0.0,
    val photo: String = "",
    val category: Category?
)