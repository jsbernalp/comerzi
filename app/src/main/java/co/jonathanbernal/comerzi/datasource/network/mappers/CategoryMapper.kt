package co.jonathanbernal.comerzi.datasource.network.mappers

import co.jonathanbernal.comerzi.datasource.network.models.ResponseCategory
import co.jonathanbernal.comerzi.ui.models.Category

fun List<ResponseCategory>.toCategories(): List<Category> {
    return this.map { it.toMap() }
}

fun ResponseCategory.toMap(): Category {
    return Category(id = this.id, name = this.categoryName)
}