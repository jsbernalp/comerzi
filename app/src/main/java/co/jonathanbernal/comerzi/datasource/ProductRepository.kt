package co.jonathanbernal.comerzi.datasource

import android.net.Uri
import co.jonathanbernal.comerzi.datasource.network.firestoreApi.ProductFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.datasource.storage.FirebaseStorageManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productFireStoreApi: ProductFireStoreApi,
    private val firebaseStorageManager: FirebaseStorageManager
) {

    fun deleteProductFromFireStore(idProduct: String): Flow<Result<Void>> {
        return productFireStoreApi.deleteProduct(idProduct)
    }

    private suspend fun uploadPhotoToStorage(
        path: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firebaseStorageManager.uploadImageToStorage(path, { uri ->
            onSuccess(uri)
        }, { exception ->
            onError(exception)
        })
    }

    suspend fun addProductToFireStore(
        product: FireStoreProduct,
        newProduct: (Flow<Result<DocumentReference>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        uploadPhotoToStorage(product.photo, { url ->
            product.photo = url
            newProduct(productFireStoreApi.addProduct(product))
        }, { exception ->
            onError(exception)
        })
    }

    fun getProductsFromFireStore(): Flow<List<FireStoreProductResponse>> {
        return productFireStoreApi.getProducts()
    }
}