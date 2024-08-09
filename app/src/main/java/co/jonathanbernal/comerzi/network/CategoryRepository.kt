package co.jonathanbernal.comerzi.network

import co.jonathanbernal.comerzi.network.api.CategoryApi
import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.network.models.ResponseCategory
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi
){

    suspend fun saveNewCategory(requestCategory: RequestCategory): ResponseCategory {
      return categoryApi.saveCategory(requestCategory)
    }

}