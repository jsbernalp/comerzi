package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.ProductRepository
import co.jonathanbernal.comerzi.datasource.network.mappers.toFireStoreCategory
import co.jonathanbernal.comerzi.datasource.network.mappers.toProductModelList
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    fun deleteProduct(idProduct: String): Flow<Result<Void>> {
        return productRepository.deleteProductFromFireStore(idProduct)
    }

    fun addProduct(
        name: String,
        ean: String,
        price: Double,
        photo: String,
        category: Category
    ): Flow<Result<DocumentReference>> {
        val product = getProduct(name, ean, price, photo, category)
        return productRepository.addProductToFireStore(product)
    }

    fun getProducts(): Flow<List<Product>> {
        return productRepository.getProductsFromFireStore().map { it.toProductModelList() }
    }

    private fun getProduct(
        name: String,
        ean: String,
        price: Double,
        photo: String,
        categoryValue: Category
    ): FireStoreProduct {
        return FireStoreProduct(
            name = name,
            ean = ean,
            price = price,
            photo = photo,
            category = categoryValue.toFireStoreCategory()
        )
    }
}