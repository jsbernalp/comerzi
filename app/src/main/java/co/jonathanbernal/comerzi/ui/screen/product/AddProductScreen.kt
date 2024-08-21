package co.jonathanbernal.comerzi.ui.screen.product

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import co.jonathanbernal.comerzi.R
import co.jonathanbernal.comerzi.ui.screen.camera.CameraPreview
import co.jonathanbernal.comerzi.ui.screen.category.CustomTextField
import co.jonathanbernal.comerzi.ui.screen.common.TopBarText
import co.jonathanbernal.comerzi.utils.orEmpty
import co.jonathanbernal.comerzi.viewModels.product.AddProductViewModel
import co.jonathanbernal.comerzi.viewModels.product.FieldType
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    addProductViewModel: AddProductViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    addProductViewModel.getCategories()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    TopBarText(text = stringResource(id = R.string.add_product_screen_title))
                },
            )
        },
        content = { contentInnerPadding ->
            val openCamera by addProductViewModel.openCamera.collectAsState()
            if (openCamera) {
                CameraPreview(innerPadding = innerPadding) {
                    Log.e("AddProductScreen", "uri de la imagen: $it")
                    addProductViewModel.updateProductData(it, FieldType.PHOTO)
                    addProductViewModel.openCamera(false)
                }
            } else {
                FormProduct(
                    addProductViewModel,
                    contentInnerPadding,
                    keyboardController,
                    navController
                )
            }
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                enabled = addProductViewModel.buttonEnabled.collectAsState().value,
                onClick = {
                    addProductViewModel.saveProduct {
                        navController.popBackStack()
                    }
                }) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(id = R.string.save_product_button_label)
                )
            }
        }
    )
}

@Composable
fun FormProduct(
    addProductViewModel: AddProductViewModel,
    innerPadding: PaddingValues,
    keyboardController: SoftwareKeyboardController?,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        val photo by addProductViewModel.photo.collectAsState()
        CustomPhotoProduct(photo, addProductViewModel)
        val productNameValue by addProductViewModel.productName.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textFieldValue = productNameValue,
            label = stringResource(id = R.string.name_product_form_label),
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
            label = stringResource(id = R.string.code_product_form_label),
            onValueChange = { newValue ->
                addProductViewModel.updateProductData(newValue, FieldType.EAN)
            },
            keyboardController
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                addProductViewModel.openScanner()
            }) {
            Text(text = stringResource(id = R.string.code_product_button_form_label))
        }

        val productPriceValue by addProductViewModel.productPrice.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textFieldValue = productPriceValue,
            label = stringResource(id = R.string.price_product_form_label),
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
    }
}

@Composable
fun CustomPhotoProduct(photo: Uri?, addProductViewModel: AddProductViewModel) {
    photo?.let {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(200.dp),
            model = photo,
            contentDescription = "Image",
            placeholder = painterResource(id = R.drawable.baseline_warning_24),
            contentScale = ContentScale.Crop,
        )
    } ?: run {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(Color.Gray),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            onClick = {
                addProductViewModel.openCamera(true)
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    imageVector = Icons.Default.PhotoCamera, contentDescription = null
                )
                Text(text = "Agregar foto")
            }
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
                text = stringResource(id = R.string.warning_category_form_label),
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
            label = { Text(stringResource(id = R.string.category_product_form_label)) },
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

