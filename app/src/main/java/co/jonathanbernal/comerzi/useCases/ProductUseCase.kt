package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.ProductRepository
import co.jonathanbernal.comerzi.datasource.local.mapper.toProduct
import co.jonathanbernal.comerzi.datasource.local.models.ProductTable
import co.jonathanbernal.comerzi.ui.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend fun addProduct(product: Product): Result<Unit> {
        val isExist = productRepository.getProductByEan(product.ean)
        return isExist?.let {
            Result.failure(Exception("Product already exists"))
        } ?: run {
            Result.success(productRepository.addProduct(product.toProductTable()))
        }
    }

    fun getAllProducts(): Flow<List<Product>> = productRepository.getAllProducts().map { list ->
        list.map { it.toProduct() }
    }

    private fun Product.toProductTable() = ProductTable(
        name = name,
        ean = ean,
        price = price
    )
}