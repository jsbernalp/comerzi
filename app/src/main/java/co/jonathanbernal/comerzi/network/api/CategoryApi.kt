package co.jonathanbernal.comerzi.network.api

import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.network.models.ResponseCategory
import co.jonathanbernal.comerzi.network.services.CategoryServices
import javax.inject.Inject

class CategoryApi @Inject constructor(
    private val categoryService: CategoryServices
) {

    suspend fun saveCategory(requestCategory: RequestCategory): ResponseCategory {
       return categoryService.addCategory(requestCategory)
    }

}