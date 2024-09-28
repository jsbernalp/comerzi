package co.jonathanbernal.comerzi.datasource.network.mappers

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategory
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategoryResponse
import co.jonathanbernal.comerzi.datasource.network.models.ResponseCategory
import co.jonathanbernal.comerzi.ui.models.Category
import java.util.Date

fun List<ResponseCategory>.toCategories(): List<Category> {
    return this.map { it.toMap() }
}

fun ResponseCategory.toMap(): Category {
    return Category(id = this.id.toString(), name = this.categoryName)
}

fun Category.toFireStoreCategory(): FireStoreCategory {
    return FireStoreCategory(id = this.id, name = this.name)
}

fun FireStoreCategory.toCategory(): Category {
    return Category(id = this.id.toString(), name = this.name)
}


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