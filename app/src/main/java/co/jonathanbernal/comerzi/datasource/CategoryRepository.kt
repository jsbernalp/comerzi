package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.local.dao.CategoryDatabaseDao
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import co.jonathanbernal.comerzi.datasource.network.api.CategoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi,
    private val categoryDatabaseDao: CategoryDatabaseDao
) {

    suspend fun addCategory(category: CategoryTable) = categoryDatabaseDao.insertCategory(category)
    suspend fun updateCategory(category: CategoryTable) =
        categoryDatabaseDao.updateCategory(category)

    suspend fun deleteCategoryFromDb(id: Int) =
        categoryDatabaseDao.deleteCategory(id)

    suspend fun deleteAllCategories() = categoryDatabaseDao.deleteAllCategories()

    fun getAllCategories(): Flow<List<CategoryTable>> =
        categoryDatabaseDao.getAllCategories().flowOn(Dispatchers.IO).conflate()

}