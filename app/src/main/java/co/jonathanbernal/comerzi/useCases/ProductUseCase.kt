package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.ProductRepository
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {


    suspend fun deleteProduct(product: Product) {
        productRepository.deleteProduct(product.idProduct)
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