package co.jonathanbernal.comerzi.viewModels.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.useCases.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _products = MutableStateFlow(listOf<Product>())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun getAllProducts() {
        viewModelScope.launch {
            productUseCase.getAllProducts().distinctUntilChanged()
                .collect { productList ->
                    _products.value = productList
                }
        }
    }
}
