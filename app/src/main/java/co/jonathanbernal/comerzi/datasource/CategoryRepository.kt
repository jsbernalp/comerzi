package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.local.dao.CategoryDao
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    suspend fun addCategory(category: CategoryTable): Result<Unit> {
        val isExist = categoryDao.getCategoryByName(category.categoryName)
        return isExist?.let {
            Result.failure(Exception("Category already exists"))
        } ?: run {
            Result.success(categoryDao.insertCategory(category))
        }
    }

    suspend fun updateCategory(category: CategoryTable) =
        categoryDao.updateCategory(category)

    suspend fun deleteCategoryFromDb(id: Int) =
        categoryDao.deleteCategory(id)

    suspend fun deleteAllCategories() = categoryDao.deleteAllCategories()

    fun getAllCategories(): Flow<List<CategoryTable>> =
        categoryDao.getAllCategories().flowOn(Dispatchers.IO).conflate()

}