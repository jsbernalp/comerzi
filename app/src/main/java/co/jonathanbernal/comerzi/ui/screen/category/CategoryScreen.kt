package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme
import co.jonathanbernal.comerzi.viewModels.category.CategoryViewModel

@Composable
fun CategoryScreen(categoryViewModel: CategoryViewModel, innerPadding: PaddingValues) {
    categoryViewModel.newCategoryName("")
    categoryViewModel.getAllCategories()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .weight(4f),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                text = "Categorias Actuales",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            val sampleItems by categoryViewModel.categories.collectAsState()
            ListCategory(
                modifier = Modifier
                    .weight(8f)
                    .padding(16.dp),
                sampleItems
            ) { deleteCategory ->
                categoryViewModel.deleteCategory(deleteCategory.id)
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
                .weight(1f),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = "Deseas agregar una nueva categoria?",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                AddCategoryBox(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 5.dp, horizontal = 16.dp),
                    categoryViewModel = categoryViewModel,
                    keyboardController
                )
            }
        }
    }
}

@Composable
fun AddCategoryBox(
    modifier: Modifier,
    categoryViewModel: CategoryViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val categoryValue by categoryViewModel.category.collectAsState()
        CustomTextField(
            modifier = Modifier
                .padding(PaddingValues(end = 10.dp))
                .wrapContentHeight()
                .weight(3f),
            textFieldValue = categoryValue,
            label = "Nombre de la categoria",
            onValueChange = { newValue ->
                categoryViewModel.newCategoryName(newValue)
            },
            keyboardController
        )
        Button(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(PaddingValues(start = 10.dp))
                .weight(1f),
            enabled = categoryViewModel.buttonEnabled.collectAsState().value,
            onClick = { categoryViewModel.addCategory() })
        {
            Text(text = "+")
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    label: String,
    onValueChange: (String) -> Unit = { },
    keyboardController: SoftwareKeyboardController?
) {
    OutlinedTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { newText -> onValueChange(newText) },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
    )
}

@Composable
fun ListCategory(
    modifier: Modifier,
    itemsCategory: List<Category>,
    onDeleteClick: (Category) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        items(itemsCategory) { item ->
            ItemRow(item, onDeleteClick = { onDeleteClick(item) })
        }
    }
}

@Composable
fun ItemRow(item: Category, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComerziTheme {
        // CategoryScreen(categoryViewModel)
    }
}