package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.ProductRepository
import co.jonathanbernal.comerzi.datasource.local.mapper.toProduct
import co.jonathanbernal.comerzi.datasource.local.mapper.toProductTable
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    suspend fun addProduct(
        name: String,
        ean: String,
        price: Double,
        photo: String,
        category: Category?
    ): Result<Unit> {
        category?.let { categoryValue ->
            val isExist = productRepository.getProductByEan(ean)
            return isExist?.let {
                Result.failure(Exception("Product already exists"))
            } ?: run {
                Result.success(
                    productRepository.addProduct(
                        getProduct(
                            name,
                            ean,
                            price,
                            photo,
                            categoryValue
                        ).toProductTable()
                    )
                )
            }
        } ?: run {
            return Result.failure(Exception("Category is required"))
        }
    }

/*    fun getAllProducts(): Flow<List<Product>> =
        productRepository.getAllProducts().map { list ->
            list.toProducts()
        }*/

    suspend fun deleteProduct(product: Product) {
        productRepository.deleteProduct(product.idProduct)
    }

    fun getAllCategoriesWithProducts(): Flow<List<Product>> {
        return categoryRepository.getAllCategoriesWithProducts().map {
            it.flatMap { categoryWithProducts ->
                categoryWithProducts.products.map { productTable ->
                    productTable.toProduct(categoryWithProducts.category)
                }
            }
        }.distinctUntilChanged()
    }

    private fun getProduct(
        name: String,
        ean: String,
        price: Double,
        photo: String,
        categoryValue: Category
    ): Product {
        return Product(
            name = name,
            ean = ean,
            price = price,
            photo = photo,
            category = categoryValue
        )
    }
}