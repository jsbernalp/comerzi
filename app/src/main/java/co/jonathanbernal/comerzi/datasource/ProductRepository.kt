package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.local.dao.ProductDao
import co.jonathanbernal.comerzi.datasource.local.models.ProductTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun addProduct(product: ProductTable) = productDao.insertProduct(product)
    suspend fun getProductByEan(ean: String): ProductTable? = productDao.getProductByEan(ean)
    suspend fun deleteProduct(id: Int) = productDao.deleteProduct(id)
    suspend fun deleteAllProducts() = productDao.deleteAllProducts()

    fun getAllProducts(): Flow<List<ProductTable>> =
        productDao.getAllProducts().flowOn(Dispatchers.IO).conflate()

}