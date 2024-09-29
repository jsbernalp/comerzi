package co.jonathanbernal.comerzi.datasource.network.firestoreApi

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategoryResponse
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProduct
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreProductResponse
import co.jonathanbernal.comerzi.utils.snapshotFlow
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductFireStoreApi @Inject constructor(
    private val fireStore: FirebaseFirestore
){

    fun deleteProduct(idProduct: String): Flow<Result<Void>> {
        return fireStore.collection("products")
            .document(idProduct)
            .delete()
            .snapshotFlow()
            .map { Result.success(it) }
    }

    fun addProduct(category: FireStoreProduct): Flow<Result<DocumentReference>> {
        return fireStore.collection("products")
            .add(category)
            .snapshotFlow()
            .map { Result.success(it) }
    }


    fun getProduct(ean: String): Flow<List<FireStoreProductResponse>> {
        return fireStore.collection("products")
            .whereEqualTo("ean", ean)
            .get()
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map {
                    FireStoreProductResponse(
                        it.id,
                        it.getString("name").orEmpty(),
                        it.getString("ean").orEmpty(),
                        it.getDouble("price") ?: 0.0,
                        it.getString("photo").orEmpty(),
                        it.getString("idCategory").orEmpty()
                    )
                }
            }
    }

    fun getProducts(): Flow<List<FireStoreProductResponse>> {
        return fireStore.collection("products")
            .get()
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map {
                    FireStoreProductResponse(
                        it.id,
                        it.getString("name").orEmpty(),
                        it.getString("ean").orEmpty(),
                        it.getDouble("price") ?: 0.0,
                        it.getString("photo").orEmpty(),
                        it.getString("idCategory").orEmpty()
                    )
                }
            }
    }
}