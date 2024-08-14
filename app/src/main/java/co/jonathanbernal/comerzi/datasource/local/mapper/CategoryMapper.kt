package co.jonathanbernal.comerzi.datasource.local.mapper

import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import co.jonathanbernal.comerzi.ui.models.Category


fun List<CategoryTable>.toCategories(): List<Category> {
    return this.map { it.toCategory() }
}

fun CategoryTable.toCategory(): Category {
    return Category(
        id = this.id,
        name = this.name,
        date = this.date
    )
}