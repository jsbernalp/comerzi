package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.network.CategoryRepository
import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.network.models.ResponseCategory
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend fun saveNewCategory(name: String): ResponseCategory {
       return categoryRepository.saveNewCategory(getRequestCategory(name))
    }

    private fun getRequestCategory(name: String): RequestCategory {
        return RequestCategory(name)
    }

}