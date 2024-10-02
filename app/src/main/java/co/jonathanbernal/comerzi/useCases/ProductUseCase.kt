package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.ProductRepository
import co.jonathanbernal.comerzi.datasource.network.mappers.toCategoriesModel
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

    suspend fun addProduct(
        name: String,
        ean: String,
        price: Double,
        photo: String,
        category: Category,
        onSuccess: (Flow<Result<DocumentReference>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val product = getProduct(name, ean, price, photo, category)
        return productRepository.addProductToFireStore(product, { newProduct ->
            onSuccess(newProduct)
        }, {
            onError(it)
        })
    }

    fun getCategoryList(): Flow<List<Category>> {
        return categoryRepository.getCategoriesFromFireStore().map { it.toCategoriesModel() }
    }

    fun getProducts(categories: List<Category>): Flow<List<Product>> {
        return productRepository.getProductsFromFireStore()
            .map { it.toProductModelList(categories) }
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
            idCategory = categoryValue.id
        )
    }
}