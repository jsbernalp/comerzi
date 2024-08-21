package co.jonathanbernal.comerzi.datasource.local.mapper

import co.jonathanbernal.comerzi.datasource.local.models.ProductTable
import co.jonathanbernal.comerzi.ui.models.Product

fun List<ProductTable>.toProducts(): List<Product> =
    map { it.toProduct() }

fun ProductTable.toProduct() = Product(
    name = name,
    ean = ean,
    price = price,
    photo = photo,
    category = category.toCategory()
)

fun Product.toProductTable() = ProductTable(
    name = name,
    ean = ean,
    price = price,
    photo = photo,
    category = category.toCategoryTable()
)