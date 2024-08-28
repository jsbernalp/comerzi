package co.jonathanbernal.comerzi.viewModels.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.useCases.CategoryUseCase
import co.jonathanbernal.comerzi.useCases.ProductUseCase
import co.jonathanbernal.comerzi.utils.orEmpty
import co.jonathanbernal.comerzi.utils.toStateFlow
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
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
    NAME, EAN, PRICE, CATEGORY, PHOTO
}

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val categoryUseCase: CategoryUseCase,
    private val scanner: GmsBarcodeScanner
) : ViewModel() {

    private val _productName = MutableStateFlow("")
    val productName: StateFlow<String> = _productName.asStateFlow()

    private val _productEan = MutableStateFlow("")
    val productEan: StateFlow<String> = _productEan.asStateFlow()

    private val _productPrice = MutableStateFlow("")
    val productPrice: StateFlow<String> = _productPrice.asStateFlow()

    private val _categories = MutableStateFlow(listOf<Category>())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _categorySelected = MutableStateFlow<Category?>(null)
    val categorySelected: StateFlow<Category?> = _categorySelected.asStateFlow()

    private val _warningCategories = MutableStateFlow(false)
    val warningCategories: StateFlow<Boolean> = _warningCategories.asStateFlow()

    private val _photo = MutableStateFlow<String>("")
    val photo: StateFlow<String> = _photo.asStateFlow()

    private val _openCamera = MutableStateFlow(false)
    val openCamera: StateFlow<Boolean> = _openCamera.asStateFlow()

    fun updateProductData(value: Any?, fieldType: FieldType) {
        when (fieldType) {
            FieldType.NAME -> _productName.value = value as String
            FieldType.EAN -> _productEan.value = value as String
            FieldType.PRICE -> _productPrice.value = value as String
            FieldType.CATEGORY -> _categorySelected.value = value as Category?
            FieldType.PHOTO -> _photo.value = value as String
        }
    }

    fun openCamera(newValue: Boolean) {
        _openCamera.value = newValue
    }

    fun getCategories() {
        viewModelScope.launch {
            categoryUseCase.getAllCategories().collect { categories ->
                _categories.value = categories
                _warningCategories.value = categories.isEmpty()
            }
        }
    }

    fun saveProduct(callback: (Unit) -> Unit) {
        viewModelScope.launch {
            productUseCase.addProduct(
                _productName.value,
                _productEan.value,
                _productPrice.value.toDouble(),
                _photo.value.toString(),
                _categorySelected.value
            ).fold(
                onSuccess = {
                    updateProductData("", FieldType.NAME)
                    updateProductData("", FieldType.EAN)
                    updateProductData("", FieldType.PRICE)
                    updateProductData(null, FieldType.CATEGORY)
                    callback.invoke(Unit)
                },
                onFailure = {}
            )
        }
    }

    fun openScanner() {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                updateProductData(barcode.displayValue.orEmpty(), FieldType.EAN)
            }
            .addOnFailureListener { e ->
                Log.e("Scanner", e.message.orEmpty())
            }
    }

    val buttonEnabled: StateFlow<Boolean> =
        combine(
            _productName,
            _productEan,
            _productPrice,
            _photo,
            _categorySelected
        ) { name, ean, price, photo, category ->
            name.isNotBlank() && ean.isNotBlank() && price.isNotBlank() && photo.isNotBlank() && category != null
        }.toStateFlow(CoroutineScope(coroutineContext), false)

}