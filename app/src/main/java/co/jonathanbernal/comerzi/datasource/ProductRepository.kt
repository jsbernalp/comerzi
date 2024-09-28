package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.network.firestoreApi.ProductFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productFireStoreApi: ProductFireStoreApi
) {

    fun deleteProductFromFireStore(idProduct: String): Flow<Result<Void>> {
        return productFireStoreApi.deleteProduct(idProduct)
    }

    fun addProductToFireStore(product: FireStoreProduct): Flow<Result<DocumentReference>> {
       return productFireStoreApi.addProduct(product)
    }

    fun getProductsFromFireStore(): Flow<List<FireStoreProductResponse>> {
        return productFireStoreApi.getProducts()
    }

}