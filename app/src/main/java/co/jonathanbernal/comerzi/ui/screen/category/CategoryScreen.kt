package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.jonathanbernal.comerzi.ui.models.Category
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme
import co.jonathanbernal.comerzi.viewModels.CategoryViewModel

@Composable
fun CategoryScreen(categoryViewModel: CategoryViewModel) {
    categoryViewModel.newCategoryName("")
    categoryViewModel.getCategoryList()
    ConstraintLayout(
        Modifier
            .wrapContentSize()
            .padding(5.dp)
    ) {
        val (addCategory, listCategory) = createRefs()
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp)
                .constrainAs(listCategory) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(addCategory.top)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
        ) {
            val sampleItems by categoryViewModel.categories.collectAsState()
            ListCategory(sampleItems) { deleteCategory ->
                categoryViewModel.deleteCategory(deleteCategory.id)
            }
        }

        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp)
                .constrainAs(addCategory) {
                    top.linkTo(listCategory.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
        ) {
            AddCategoryBox(categoryViewModel)
        }
    }
}

@Composable
fun AddCategoryBox(categoryViewModel: CategoryViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        ConstraintLayout {
            val categoryValue by categoryViewModel.category.collectAsState()
            val (field, button) = createRefs()
            CustomTextField(
                modifier = Modifier
                    .padding(PaddingValues(end = 10.dp))
                    .wrapContentHeight()
                    .constrainAs(field) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(button.absoluteLeft)
                    },
                textFieldValue = categoryValue,
                label = "Nombre de la categoria",
                onValueChange = { newValue ->
                    categoryViewModel.newCategoryName(newValue)
                }
            )
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(PaddingValues(start = 10.dp))
                    .constrainAs(button) {
                        start.linkTo(field.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.absoluteRight)
                        bottom.linkTo(parent.bottom)
                    },
                enabled = categoryViewModel.buttonEnabled.collectAsState().value,
                onClick = { categoryViewModel.saveNewCategory() })
            {
                Text(text = "+")
            }
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    label: String,
    onValueChange: (String) -> Unit = { },
) {
    OutlinedTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { newText -> onValueChange(newText) },
        label = { Text(text = label) },
    )
}

@Composable
fun ListCategory(itemsCategory: List<Category>, onDeleteClick: (Category) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .padding(16.dp)
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
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
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