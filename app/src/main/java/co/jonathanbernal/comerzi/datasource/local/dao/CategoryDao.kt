package co.jonathanbernal.comerzi.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * from categoryTable")
    fun getAllCategories(): Flow<List<CategoryTable>>

    @Query("SELECT * from categoryTable WHERE idCategory = :id")
    suspend fun getCategoryById(id: Int): CategoryTable

    @Query("SELECT * from categoryTable WHERE categoryName = :name")
    suspend fun getCategoryByName(name: String): CategoryTable?

    @Insert
    suspend fun insertCategory(categoryTable: CategoryTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryTable: CategoryTable)

    @Query("DELETE from categoryTable WHERE idCategory = :id")
    suspend fun deleteCategory(id: Int)

    @Query("DELETE from categoryTable")
    suspend fun deleteAllCategories()
}