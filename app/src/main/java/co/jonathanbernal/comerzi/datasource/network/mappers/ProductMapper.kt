package co.jonathanbernal.comerzi.datasource.network.mappers

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product

fun List<FireStoreProductResponse>.toProductModelList(categoryList: List<Category>): List<Product> {
    return this.map { it.toProductModel(categoryList) }
}

fun FireStoreProductResponse.toProductModel(categoryList: List<Category>): Product {
    return Product(
        idProduct = this.id,
        name = this.name,
        ean = this.ean,
        price = this.price,
        photo = this.photo,
        category = getCategoryById(this.idCategory, categoryList)
    )
}


fun getCategoryById(idCategory: String, categoryList: List<Category>): Category {
    return categoryList.find { it.id == idCategory } ?: Category("", "")
}