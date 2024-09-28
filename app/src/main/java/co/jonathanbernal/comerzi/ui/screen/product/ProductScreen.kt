package co.jonathanbernal.comerzi.ui.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.jonathanbernal.comerzi.R
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.utils.orEmpty
import co.jonathanbernal.comerzi.viewModels.product.ProductViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal


@Composable
fun ProductScreen(
    innerPadding: PaddingValues,
    navigateTo: (Unit) -> Unit
) {
    val productViewModel = hiltViewModel<ProductViewModel>()
    productViewModel.getAllProducts()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        content = { _ ->
            ContentProductList(productViewModel)
        },
        floatingActionButton = { AddProduct(navigateTo) },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
fun AddProduct(navigateTo: (Unit) -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { navigateTo.invoke(Unit) },
        icon = {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_product_button_label)
            )
        },
        text = { Text(text = stringResource(id = R.string.add_product_button_label)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentProductList(
    productViewModel: ProductViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        SearchProduct(productViewModel)
        val products by productViewModel.products.collectAsState()
        ListProduct(
            modifier = Modifier,
            itemsCategory = products,
            onDeleteClick = {
                showBottomSheet = true
                productViewModel.updateProductSelected(it)
            }
        )
    }

    if (showBottomSheet) {
        DeleteDialog(productViewModel, sheetState, scope) {
            showBottomSheet = it
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeleteDialog(
    productViewModel: ProductViewModel,
    sheetState: SheetState,
    scope: CoroutineScope,
    showBottomSheet: (Boolean) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            productViewModel.updateProductSelected(null)
            showBottomSheet(false)
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(id = R.string.delete_product_title),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomButton(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    icon = Icons.Default.Cancel,
                    textButton = stringResource(R.string.cancel_button_label)
                ) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            productViewModel.updateProductSelected(null)
                            showBottomSheet(false)
                        }
                    }
                }
                CustomButton(
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.5f)
                    ),
                    icon = Icons.Default.Delete,
                    textButton = stringResource(R.string.delete_button_label)
                ) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            productViewModel.deleteProduct()
                            showBottomSheet(false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomButton(colors: ButtonColors, icon: ImageVector, textButton: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        }, colors = colors
    ) {
        Icon(
            modifier = Modifier.padding(end = 8.dp),
            imageVector = icon,
            contentDescription = null
        )
        Text(textButton)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchProduct(
    productViewModel: ProductViewModel
) {
    val searchText by productViewModel.searchText.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        verticalAlignment = Alignment.Bottom
    ) {
        SearchBar(
            modifier = Modifier
                .weight(5f)
                .padding(8.dp),
            query = searchText,
            onQueryChange = { productViewModel.onSearchTextChange(it) },
            onSearch = { Unit },
            active = false,
            onActiveChange = { Unit },
            placeholder = {
                Text(text = stringResource(id = R.string.search_product_hint))
            }
        ) {}
        IconButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onClick = { productViewModel.openScanner() }) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.QrCodeScanner, contentDescription = null
            )
        }
    }
}

@Composable
fun ListProduct(
    modifier: Modifier,
    itemsCategory: List<Product>,
    onDeleteClick: (Product) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(itemsCategory) { item ->
            ItemProduct(item, onDeleteClick = { onDeleteClick(item) })
        }
    }
}

@Composable
fun ItemProduct(item: Product, onDeleteClick: (Product) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .height(200.dp),
                    model = item.photo,
                    contentDescription = "Image",
                    placeholder = painterResource(id = R.drawable.baseline_warning_24),
                    contentScale = ContentScale.Crop,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextWithLabel(
                        horizontalAlignment = Alignment.Start,
                        labelValue = stringResource(id = R.string.label_product_name_card),
                        textValue = item.name
                    )
                    TextWithLabel(
                        horizontalAlignment = Alignment.End,
                        labelValue = stringResource(id = R.string.label_product_price_card),
                        textValue = BigDecimal(item.price).toString()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextWithLabel(
                        horizontalAlignment = Alignment.Start,
                        labelValue = stringResource(id = R.string.label_product_ean_card),
                        textValue = item.ean
                    )
                    TextWithLabel(
                        horizontalAlignment = Alignment.End,
                        labelValue = stringResource(id = R.string.label_product_category_card),
                        textValue = item.category?.name.orEmpty()
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(item) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun TextWithLabel(
    horizontalAlignment: Alignment.Horizontal,
    labelValue: String,
    textValue: String
) {
    Column(
        modifier = Modifier.width(150.dp),
        horizontalAlignment = horizontalAlignment,
    ) {
        Text(text = labelValue, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = textValue,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 440)
@Composable
fun ProductCardPreview() {
    val itemProduct = Product(
        name = "Product 1",
        ean = "123456789",
        price = 100.0,
        photo = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
        category = Category("Category 1", id = "1")
    )
    ItemProduct(itemProduct, onDeleteClick = {})
}
