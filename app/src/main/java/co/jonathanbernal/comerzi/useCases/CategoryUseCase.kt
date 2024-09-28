package co.jonathanbernal.comerzi.useCases

import co.jonathanbernal.comerzi.datasource.CategoryRepository
import co.jonathanbernal.comerzi.datasource.network.mappers.toCategoriesModel
import co.jonathanbernal.comerzi.datasource.network.models.FireStoreCategory
import co.jonathanbernal.comerzi.ui.models.Category
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    fun getAllFireStoreCategories(): Flow<List<Category>> {
        return categoryRepository.getCategoriesFromFireStore().map {
            it.toCategoriesModel()
        }
    }

    suspend fun addCategoryToFireStore(categoryName: String): Result<DocumentReference> {
        return categoryRepository.addCategoryToFireStore(
            FireStoreCategory(name = categoryName.uppercase())
        )
    }

    fun deleteCategoryToFireStore(idCategory: String): Flow<Result<Void>> {
        return categoryRepository.deleteCategoryFromFireStore(idCategory)
    }

    fun updateCategoryFromFireStore(category: Category): Flow<Result<Void>> {
        return categoryRepository.updateCategoryFromFireStore(category)
    }
}