package co.jonathanbernal.comerzi.viewModels.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.useCases.ProductUseCase
import co.jonathanbernal.comerzi.utils.orEmpty
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase, private val scanner: GmsBarcodeScanner
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _productSelected = MutableStateFlow<Product?>(null)
    val productSelected: StateFlow<Product?> = _productSelected.asStateFlow()

    private val _products = MutableStateFlow(listOf<Product>())
    val products: StateFlow<List<Product>> = searchText.combine(_products) { text, products ->
        if (text.isBlank()) {
            products
        } else {
            products.filter { product ->
                isContain(product, text)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _products.value
    )

    private fun isContain(
        product: Product, text: String
    ): Boolean {
        val searchForEan = product.ean.contains(text, ignoreCase = true)
        val searchForName = product.name.contains(text, ignoreCase = true)
        return searchForEan || searchForName
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun openScanner() {
        scanner.startScan().addOnSuccessListener { barcode ->
            onSearchTextChange(barcode.displayValue.orEmpty())
        }.addOnFailureListener { e ->
            Log.e("Scanner", e.message.orEmpty())
        }
    }

    fun updateProductSelected(product: Product?) {
        _productSelected.value = product
    }

    fun deleteProduct() {
        viewModelScope.launch {
            productSelected.value?.let { product ->
                productUseCase.deleteProduct(product)
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productUseCase.getAllCategoriesWithProducts().distinctUntilChanged()
                .collect { productList ->
                    _products.value = emptyList()
                    _products.value = productList
                }
        }
    }
}
