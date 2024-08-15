package co.jonathanbernal.comerzi.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.jonathanbernal.comerzi.datasource.local.models.ProductTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * from productTable")
    fun getAllProducts(): Flow<List<ProductTable>>

    @Query("SELECT * from productTable WHERE ean = :ean")
    suspend fun getProductByEan(ean: String): ProductTable?

    @Insert
    suspend fun insertProduct(productTable: ProductTable)

    @Query("DELETE from productTable WHERE idProduct = :id")
    suspend fun deleteProduct(id: Int)

    @Query("DELETE from productTable")
    suspend fun deleteAllProducts()

}