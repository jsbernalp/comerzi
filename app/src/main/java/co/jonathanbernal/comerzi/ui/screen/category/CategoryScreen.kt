package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.screen.product.TextWithLabel
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme
import co.jonathanbernal.comerzi.viewModels.category.CategoryViewModel

@Composable
fun CategoryScreen(innerPadding: PaddingValues) {
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    categoryViewModel.newCategoryName("")
    categoryViewModel.getAllCategories()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        content = { innerPadding ->
            val editCategory by categoryViewModel.editCategory.collectAsState()
            editCategory?.let { categoryEdit ->
                EditCategoryDialog(innerPadding, categoryEdit, keyboardController, {
                    categoryViewModel.editCategory(null)
                }, { newValue ->
                    categoryViewModel.editCategory(
                        Category(
                            name = newValue,
                            id = categoryEdit.id,
                            date = categoryEdit.date
                        )
                    )
                }, {
                    categoryViewModel.saveEditCategory()
                })
            }
            ContentCategoryScreen(categoryViewModel, innerPadding, keyboardController)
        }
    )
}

@Composable
private fun ContentCategoryScreen(
    categoryViewModel: CategoryViewModel,
    innerPadding: PaddingValues,
    keyboardController: SoftwareKeyboardController?
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        AddCategoryCard(categoryViewModel, keyboardController)
        val sampleItems by categoryViewModel.categories.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "Listado de Categorias".uppercase(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        ListCategory(
            sampleItems,
            { deleteCategory ->
                categoryViewModel.deleteCategory(deleteCategory.id)
            },
            { editCategory ->
                categoryViewModel.editCategory(editCategory)
            })
    }
}

@Composable
fun AddCategoryCard(
    categoryViewModel: CategoryViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
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
            Spacer(modifier = Modifier.height(16.dp))
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
    itemsCategory: List<Category>,
    onDeleteClick: (Category) -> Unit,
    onEditClick: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.Top
    ) {
        items(itemsCategory) { item ->
            ItemRow(item,
                onDeleteClick = { onDeleteClick(item) },
                onEditClick = { onEditClick(item) })
        }
    }
}

@Composable
fun ItemRow(
    item: Category,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { onEditClick() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Delete Item")
                }
                TextWithLabel(
                    horizontalAlignment = Alignment.Start,
                    labelValue = "Nombre",
                    textValue = item.name
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Item")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComerziTheme {
        // CategoryScreen(categoryViewModel)
    }
}

@Composable
fun EditCategoryDialog(
    innerPadding: PaddingValues,
    editCategory: Category,
    keyboardController: SoftwareKeyboardController?,
    onDismissRequest: () -> Unit,
    onChangeCategoryName: (String) -> Unit,
    onSaveCategory: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(innerPadding),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 16.dp)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTextField(
                    modifier = Modifier
                        .padding(PaddingValues(horizontal = 16.dp))
                        .wrapContentHeight(),
                    textFieldValue = editCategory.name,
                    label = "Nombre de la categoria",
                    onValueChange = { newValue ->
                        onChangeCategoryName(newValue)
                    },
                    keyboardController
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancelar")
                    }
                    TextButton(
                        onClick = { onSaveCategory() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}