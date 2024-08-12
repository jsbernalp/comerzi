package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme

@Composable
fun CategoryScreenTest() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
                .weight(5f),
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
            val sampleItems = listOf(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 5",
                "Item 3",
                "Item 4",
                "Item 5"
            )
            ListCategoryTest(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(8f)
                    .padding(16.dp),
                sampleItems
            ) { clickedItem ->
                println("Clic en $clickedItem")
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
                .weight(2f),
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
                AddCategoryBoxTest(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun AddCategoryBoxTest(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextField(
            modifier = Modifier
                .padding(PaddingValues(end = 10.dp))
                .wrapContentHeight()
                .weight(3f),
            textFieldValue = "",
            label = "Nombre de la categoria",
            onValueChange = { }
        )
        Button(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .weight(1f),
            enabled = false,
            onClick = { })
        {
            Text(text = "+")
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
fun ListCategoryTest(
    modifier: Modifier,
    itemsCategory: List<String>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
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
        textAlign = TextAlign.Justify,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    )
}

@Preview(
    showBackground = true,
    widthDp = 400,
    heightDp = 700
)
@Composable
fun DefaultPreviewTest() {
    ComerziTheme {
        CategoryScreenTest()
    }
}