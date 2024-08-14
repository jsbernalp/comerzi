package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.local.mapper.toCategories
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import co.jonathanbernal.comerzi.ui.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    fun getAllCategories(): Flow<List<Category>> {
        return categoryRepository.getAllCategories().map {
            it.toCategories()
        }
    }

    suspend fun addCategory(categoryName: String): Result<Unit> {
        return categoryRepository.addCategory(CategoryTable(name = categoryName.uppercase()))
    }

    suspend fun deleteCategoryFromDb(id: Int) {
        categoryRepository.deleteCategoryFromDb(id)
    }
}