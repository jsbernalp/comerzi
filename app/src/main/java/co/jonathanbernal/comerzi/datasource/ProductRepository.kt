package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.network.firestoreApi.CategoryFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.firestoreApi.ProductFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.mappers.toCategoryModel
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.ui.models.Category
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productFireStoreApi: ProductFireStoreApi,
    private val categoryFireStoreApi: CategoryFireStoreApi
) {

    private var categoryList = mutableListOf<Category>()

    fun deleteProductFromFireStore(idProduct: String): Flow<Result<Void>> {
        return productFireStoreApi.deleteProduct(idProduct)
    }

    fun addProductToFireStore(product: FireStoreProduct): Flow<Result<DocumentReference>> {
       return productFireStoreApi.addProduct(product)
    }

    fun getProductsFromFireStore(): Flow<List<FireStoreProductResponse>> {
        return productFireStoreApi.getProducts()
    }

    suspend fun updateCategoryList() {
        categoryFireStoreApi.getCategories().collect{ response ->
            categoryList = response.map { it.toCategoryModel() }.toMutableList()
        }
    }
}