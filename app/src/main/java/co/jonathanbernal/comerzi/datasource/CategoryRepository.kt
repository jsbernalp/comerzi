package co.jonathanbernal.comerzi.datasource

import co.jonathanbernal.comerzi.datasource.network.firestoreApi.CategoryFireStoreApi
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategory
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategoryResponse
import co.jonathanbernal.comerzi.ui.models.Category
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryFireStoreApi: CategoryFireStoreApi
) {

     fun updateCategoryFromFireStore(category: Category): Flow<Result<Void>> {
        return categoryFireStoreApi.updateCategory(category)
    }

     fun deleteCategoryFromFireStore(idCategory: String): Flow<Result<Void>> {
        return categoryFireStoreApi.deleteCategory(idCategory)
    }

    suspend fun addCategoryToFireStore(category: FireStoreCategory): Result<DocumentReference> {
        val isExist: Boolean =
            categoryFireStoreApi.getCategory(category.name).map { it.isNotEmpty() }.single()
        return if (isExist) {
            Result.failure(Exception("Category already exists"))
        } else {
            categoryFireStoreApi.addCategory(category).single()
        }
    }

     fun getCategoriesFromFireStore(): Flow<List<FireStoreCategoryResponse>> {
        return categoryFireStoreApi.getCategories()
    }
}