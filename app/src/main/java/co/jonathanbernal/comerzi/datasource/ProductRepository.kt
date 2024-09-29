package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.network.firestoreApi.CategoryFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.firestoreApi.ProductFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.mappers.toCategoryModel
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.ui.models.Category
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productFireStoreApi: ProductFireStoreApi,
    private val categoryFireStoreApi: CategoryFireStoreApi
) {

    private var categoryList = mutableListOf<Category>()

    fun deleteProductFromFireStore(idProduct: String): Flow<Result<Void>> {
        return productFireStoreApi.deleteProduct(idProduct)
    }

    suspend fun addProductToFireStore(product: FireStoreProduct): Result<DocumentReference> {
        val isExist: Boolean =
            productFireStoreApi.getProduct(product.ean).map { it.isNotEmpty() }.single()
        return if (isExist) {
            Result.failure(Exception("Category already exists"))
        } else {
            productFireStoreApi.addProduct(product).single()
        }
    }

    fun getProductsFromFireStore(): Flow<List<FireStoreProductResponse>> {
        return productFireStoreApi.getProducts()
    }

    suspend fun updateCategoryList() {
        categoryFireStoreApi.getCategories().collect { response ->
            categoryList = response.map { it.toCategoryModel() }.toMutableList()
        }
    }
}