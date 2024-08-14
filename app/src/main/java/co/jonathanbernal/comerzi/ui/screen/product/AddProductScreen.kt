package co.jonathanbernal.comerzi.ui.screen.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.jonathanbernal.comerzi.ui.screen.category.CustomTextField
import co.jonathanbernal.comerzi.viewModels.product.AddProductViewModel
import co.jonathanbernal.comerzi.viewModels.product.FieldType

@Composable
fun AddProductScreen(
    productViewModel: AddProductViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    FormProduct(productViewModel, navController, innerPadding, keyboardController)
}

@Composable
fun FormProduct(
    addProductViewModel: AddProductViewModel,
    navController: NavController,
    innerPadding: PaddingValues,
    keyboardController: SoftwareKeyboardController?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        val productNameValue by addProductViewModel.productName.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Agregar Producto",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier
                .padding(PaddingValues(end = 10.dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            textFieldValue = productNameValue,
            label = "Nombre del producto",
            onValueChange = { newValue ->
                addProductViewModel.updateProductData(newValue, FieldType.NAME)
            },
            keyboardController
        )
        val productEanValue by addProductViewModel.productEan.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier
                .padding(PaddingValues(end = 10.dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            textFieldValue = productEanValue,
            label = "Codigo de barras del producto",
            onValueChange = { newValue ->
                addProductViewModel.updateProductData(newValue, FieldType.EAN)
            },
            keyboardController
        )
        val productPriceValue by addProductViewModel.productPrice.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier
                .padding(PaddingValues(end = 10.dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            textFieldValue = productPriceValue,
            label = "Precio del producto",
            onValueChange = { newValue ->
                addProductViewModel.updateProductData(newValue, FieldType.PRICE)
            },
            keyboardController
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = addProductViewModel.buttonEnabled.collectAsState().value,
            onClick = {
                addProductViewModel.saveProduct {
                    navController.popBackStack()
                }
            }) {
            Text(text = "Guardar Producto")
        }
    }
}

