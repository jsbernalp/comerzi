package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.network.CategoryRepository
import co.jonathanbernal.comerzi.network.mappers.toCategories
import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.ui.models.Category
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend fun saveNewCategory(name: String): Result<Unit> {
        return categoryRepository.saveNewCategory(getRequestCategory(name))
    }

    suspend fun deleteCategory(id: String): Result<Unit> {
        return categoryRepository.deleteCategory(id)
    }

    suspend fun getCategories(): Result<List<Category>> {
        return categoryRepository.getCategories().map {
            it.toCategories()
        }
    }

    private fun getRequestCategory(name: String): RequestCategory {
        return RequestCategory(name)
    }

}