package co.jonathanbernal.comerzi.viewModels.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.useCases.CategoryUseCase
import co.jonathanbernal.comerzi.utils.toStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase
) : ViewModel() {

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _categories = MutableStateFlow(listOf<Category>())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _editCategory = MutableStateFlow<Category?>(null)
    val editCategory: StateFlow<Category?> = _editCategory.asStateFlow()

    fun newCategoryName(value: String) {
        _category.value = value
    }

    fun editCategory(value: Category?) {
        _editCategory.value = value
    }

    fun getAllCategories() {
        viewModelScope.launch {
            categoryUseCase.getAllCategories().distinctUntilChanged()
                .collect { listOfCategories ->
                    _categories.value = listOfCategories
                }
        }
    }

    fun addCategory() {
        viewModelScope.launch {
            categoryUseCase.addCategory(_category.value)
                .fold(
                    onSuccess = {
                        newCategoryName("")
                    },
                    onFailure = {
                        Log.e("CategoryViewModel", "Error al agregar la categoria", it)
                    }
                )
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            categoryUseCase.deleteCategoryFromDb(id)
        }
    }

    fun saveEditCategory() {
        viewModelScope.launch {
            _editCategory.value?.let { category ->
                categoryUseCase.updateCategory(category)
                editCategory(null)
            }
        }
    }

    val buttonEnabled: StateFlow<Boolean> =
        combine(
            _category
        ) { categoryValue ->
            categoryValue.first().isNotBlank()
        }.toStateFlow(CoroutineScope(coroutineContext), false)
}