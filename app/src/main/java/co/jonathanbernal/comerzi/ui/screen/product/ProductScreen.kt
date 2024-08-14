package co.jonathanbernal.comerzi.ui.screen.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.jonathanbernal.comerzi.ui.models.Product
import co.jonathanbernal.comerzi.viewModels.product.ProductViewModel


@Composable
fun ProductScreen(
    productViewModel: ProductViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    productViewModel.getAllProducts()
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
        content = { currentInnerPadding ->
            ContentProductList(productViewModel, currentInnerPadding)
        },
        floatingActionButton = { AddProduct(navController) },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
fun AddProduct(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("addProduct") }
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Product")
    }
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
                .fillMaxWidth()
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
            )
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.ean,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
            )
            Text(
                text = "Precio ${item.price}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
            )
        }
    }
}

@Preview
@Composable
fun ProductScreenPreview() {
    val products = listOf(
        Product("Producto 1", "Descripcion del producto 1", 100.0),
        Product("Producto 2", "Descripcion del producto 2", 200.0),
        Product("Producto 3", "Descripcion del producto 3", 300.0),
    )
    ItemProduct(products[0], onDeleteClick = { })
}