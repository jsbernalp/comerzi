package co.jonathanbernal.comerzi.datasource.network.mappers

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product

fun List<FireStoreProductResponse>.toProductModelList(): List<Product> {
    return this.map { it.toProductModel() }
}

fun FireStoreProductResponse.toProductModel(): Product {
    return Product(
        idProduct = this.id,
        name = this.name,
        ean = this.ean,
        price = this.price,
        photo = this.photo,
        category = this.category.toCategory()
    )
}


fun Map<String,String>.toCategory(): Category {
    return Category(id = this.values.last(), name = this.values.first())
}