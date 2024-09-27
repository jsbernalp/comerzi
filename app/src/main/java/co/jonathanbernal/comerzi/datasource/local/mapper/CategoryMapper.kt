package co.jonathanbernal.comerzi.datasource.local.mapper

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategoryResponse
import co.jonathanbernal.comerzi.ui.models.Category
import java.util.Date


fun List<FireStoreCategoryResponse>.toCategoriesModel(): List<Category> {
    return this.map { it.toCategoryModel() }
}

fun FireStoreCategoryResponse.toCategoryModel(): Category {
    return Category(
        id = this.idCategory,
        name = this.name,
        date = Date()
    )
}