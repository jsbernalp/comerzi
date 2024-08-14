package co.jonathanbernal.comerzi.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDatabaseDao {

    @Query("SELECT * from categoryTable")
    fun getAllCategories(): Flow<List<CategoryTable>>

    @Query("SELECT * from categoryTable WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryTable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(categoryTable: CategoryTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryTable: CategoryTable)

    @Query("DELETE from categoryTable WHERE id = :id")
    suspend fun deleteCategory(id: Int)

    @Query("DELETE from categoryTable")
    suspend fun deleteAllCategories()
}