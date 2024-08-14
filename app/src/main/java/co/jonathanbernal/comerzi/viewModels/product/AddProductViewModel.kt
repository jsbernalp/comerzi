package co.jonathanbernal.comerzi.viewModels.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.useCases.ProductUseCase
import co.jonathanbernal.comerzi.utils.toStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FieldType {
    NAME, EAN, PRICE
}

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _productName = MutableStateFlow("")
    val productName: StateFlow<String> = _productName.asStateFlow()

    private val _productEan = MutableStateFlow("")
    val productEan: StateFlow<String> = _productEan.asStateFlow()

    private val _productPrice = MutableStateFlow("")
    val productPrice: StateFlow<String> = _productPrice.asStateFlow()


    fun updateProductData(value: String, fieldType: FieldType) {
        when (fieldType) {
            FieldType.NAME -> _productName.value = value
            FieldType.EAN -> _productEan.value = value
            FieldType.PRICE -> _productPrice.value = value
        }
    }

    fun saveProduct(callback: (Unit) -> Unit) {
        viewModelScope.launch {
            productUseCase.addProduct(
                Product(
                    _productName.value,
                    _productEan.value,
                    _productPrice.value.toDouble()
                )
            ).fold(
                onSuccess = {
                    updateProductData("", FieldType.NAME)
                    updateProductData("", FieldType.EAN)
                    updateProductData("", FieldType.PRICE)
                    callback.invoke(Unit)
                },
                onFailure = {}
            )
        }
    }

    val buttonEnabled: StateFlow<Boolean> =
        combine(
            _productName,
            _productEan,
            _productPrice
        ) { name, ean, price ->
            name.isNotBlank() && ean.isNotBlank() && price.isNotBlank()
        }.toStateFlow(CoroutineScope(coroutineContext), false)

}