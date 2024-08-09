package co.jonathanbernal.comerzi.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.useCases.CategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase
) : ViewModel() {

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    fun newCategoryName(value: String) {
        _category.value = value
    }

    fun saveNewCategory() {
        viewModelScope.launch {
            runCatching {
                val result = categoryUseCase.saveNewCategory(category.value)
                if (result != null) {
                    newCategoryName("")
                }
            }
        }
    }


    val buttonEnabled: StateFlow<Boolean> =
        combine(
            _category
        ) { categoryValue ->
            categoryValue.first().isNotBlank()
        }.toStateFlow(CoroutineScope(coroutineContext), false)

    private fun <R> Flow<R>.toStateFlow(coroutineScope: CoroutineScope, initialValue: R) =
        stateIn(coroutineScope, SharingStarted.Lazily, initialValue)
}