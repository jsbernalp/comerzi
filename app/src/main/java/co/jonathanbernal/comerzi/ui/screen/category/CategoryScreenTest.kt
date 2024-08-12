package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme

@Composable
fun CategoryScreenTest() {
    ConstraintLayout(
        Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        val (addCategory, listCategory) = createRefs()
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp)
                .constrainAs(addCategory) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(listCategory.top)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
        ) {
            AddCategoryBoxTest()
        }
        ElevatedCard(
            modifier = Modifier
                .wrapContentHeight()
                .padding(5.dp)
                .constrainAs(listCategory) {
                    top.linkTo(addCategory.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
        ) {
            val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
            ListCategoryTest(sampleItems) { clickedItem ->
                println("Clic en $clickedItem")
            }
        }
    }
}

@Composable
fun AddCategoryBoxTest() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        ConstraintLayout(
            Modifier.padding(10.dp)
        ) {
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
                textFieldValue = "",
                label = "Nombre de la categoria",
                onValueChange = { }
            )
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .constrainAs(button) {
                        start.linkTo(field.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.absoluteRight)
                        bottom.linkTo(parent.bottom)
                    },
                enabled = false,
                onClick = { })
            {
                Text(text = "+")
            }
        }
    }
}

@Composable
fun CustomTextFieldTest(
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
fun ListCategoryTest(itemsCategory: List<String>, onItemClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        items(itemsCategory) { item ->
            ListItemTest(item, onItemClick)
        }
    }
}

@Composable
fun ListItemTest(item: String, onItemClick: (String) -> Unit) {
    Text(
        text = item,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewTest() {
    ComerziTheme {
        CategoryScreenTest()
    }
}