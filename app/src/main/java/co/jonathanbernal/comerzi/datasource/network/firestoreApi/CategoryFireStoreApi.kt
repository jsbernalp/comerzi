package co.jonathanbernal.comerzi.datasource.network.firestoreApi

import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategory
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategoryResponse
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.utils.orZero
import co.jonathanbernal.comerzi.utils.snapshotFlow
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryFireStoreApi @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun deleteCategory(idCategory: String): Flow<Result<Void>> {
        return fireStore.collection("categories")
            .document(idCategory)
            .delete()
            .snapshotFlow()
            .map { Result.success(it) }
    }

    fun addCategory(category: FireStoreCategory): Flow<Result<DocumentReference>> {
        return fireStore.collection("categories")
            .add(category)
            .snapshotFlow()
            .map { Result.success(it) }
    }

    fun getCategories(): Flow<List<FireStoreCategoryResponse>> {
        return fireStore.collection("categories")
            .get()
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map {
                    FireStoreCategoryResponse(
                        it.id,
                        it.getString("name").orEmpty()
                    )
                }
            }
    }

    fun getCategory(name: String): Flow<List<FireStoreCategoryResponse>> {
        return fireStore.collection("categories")
            .whereEqualTo("name", name)
            .get()
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map {
                    FireStoreCategoryResponse(
                        it.id,
                        it.getString("name").orEmpty()
                    )
                }
            }
    }

    fun updateCategory(category: Category): Flow<Result<Void>> {
        return fireStore.collection("categories")
            .document(category.id)
            .update("name" , category.name)
            .snapshotFlow()
            .map { Result.success(it) }

    }
}