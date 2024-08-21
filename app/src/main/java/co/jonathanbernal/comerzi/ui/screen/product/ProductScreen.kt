package co.jonathanbernal.comerzi.ui.screen.product

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.jonathanbernal.comerzi.R
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.viewModels.product.ProductViewModel
import coil.compose.AsyncImage
import java.math.BigDecimal


@Composable
fun ProductScreen(
    navController: NavController,
    innerPadding: PaddingValues
) {
    val productViewModel = hiltViewModel<ProductViewModel>()
    productViewModel.getAllProducts()
    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        content = { currentInnerPadding ->
            ContentProductList(productViewModel, currentInnerPadding)
        },
        floatingActionButton = { AddProduct(navController) },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
fun AddProduct(navController: NavController) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate("addProduct") },
        icon = {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_product_button_label)
            )
        },
        text = { Text(text = stringResource(id = R.string.add_product_button_label)) },
    )
}

@Composable
private fun ContentProductList(productViewModel: ProductViewModel, innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        val products by productViewModel.products.collectAsState()
        ListProduct(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .align(Alignment.TopStart),
            itemsCategory = products,
            onDeleteClick = { }
        )
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
fun ItemProduct(item: Product, onDeleteClick: () -> Unit) {
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
                        textValue = item.category.name
                    )
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


@Preview(showBackground = true, widthDp = 320, heightDp = 340)
@Composable
fun ProductCardPreview() {
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
                .wrapContentSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Icon(
                    painterResource(id = R.drawable.baseline_warning_24),
                    contentDescription = "warning",
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextWithLabel(
                        horizontalAlignment = Alignment.Start,
                        labelValue = "Producto",
                        textValue = "Terpel 20w - 50"
                    )
                    TextWithLabel(
                        horizontalAlignment = Alignment.End,
                        labelValue = "Precio",
                        textValue = "$10098172398173871"
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
                        labelValue = "EAN",
                        textValue = "123456789"
                    )
                    TextWithLabel(
                        horizontalAlignment = Alignment.End,
                        labelValue = "Categoria",
                        textValue = "Aceites"
                    )
                }
            }
        }
    }
}
