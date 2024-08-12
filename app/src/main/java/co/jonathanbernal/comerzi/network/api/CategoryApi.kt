package co.jonathanbernal.comerzi.network.api

import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.network.models.ResponseCategory
import co.jonathanbernal.comerzi.network.services.CategoryServices
import co.jonathanbernal.comerzi.utils.toMapResult
import javax.inject.Inject

class CategoryApi @Inject constructor(
    private val categoryService: CategoryServices
) {

    suspend fun saveCategory(requestCategory: RequestCategory): Result<Unit> {
        return categoryService.addCategory(requestCategory).toMapResult()
    }

    suspend fun deleteCategory(id: String): Result<Unit> {
        return categoryService.deleteCategory(id).toMapResult()
    }

    suspend fun getCategories(): Result<List<ResponseCategory>> {
        return categoryService.getCategories().toMapResult()
    }
}