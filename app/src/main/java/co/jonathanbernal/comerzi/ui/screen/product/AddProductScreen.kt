package co.jonathanbernal.comerzi.ui.screen.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import co.jonathanbernal.comerzi.R
import co.jonathanbernal.comerzi.ui.screen.category.CustomTextField
import co.jonathanbernal.comerzi.utils.orEmpty
import co.jonathanbernal.comerzi.viewModels.product.AddProductViewModel
import co.jonathanbernal.comerzi.viewModels.product.FieldType

@Composable
fun AddProductScreen(
    productViewModel: AddProductViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    productViewModel.getCategories()
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
        Spinner(addProductViewModel)
        val isWarningCategoriesShow by addProductViewModel.warningCategories.collectAsState()
        if (isWarningCategoriesShow) {
            AlertCategories()
        }
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

@Composable
private fun AlertCategories() {
    Spacer(modifier = Modifier.height(16.dp))
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(Color.Red),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_warning_24),
                contentDescription = "warning",
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "No olvides agregar categorias antes de agregar un producto",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
    }
}


@Composable
fun Spinner(addProductViewModel: AddProductViewModel) {
    var mExpanded by remember { mutableStateOf(false) }
    val categories by addProductViewModel.categories.collectAsState()
    val categorySelected =
        addProductViewModel.categorySelected.collectAsState().value?.name.orEmpty()

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = categorySelected.orEmpty(),
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Seleccionar Categoria") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            },
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                mExpanded = !mExpanded
                            }
                        }
                    }
                }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) {
                    mTextFieldSize.width.toDp()
                })
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    addProductViewModel.updateProductData(category, FieldType.CATEGORY)
                    mExpanded = false
                }, text = { Text(text = category.name) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    Spacer(modifier = Modifier.height(16.dp))
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        colors = CardDefaults.cardColors(Color.Red),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_warning_24),
                contentDescription = "warning",
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "No olvides agregar categorias antes de agregar un producto",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
    }
}

