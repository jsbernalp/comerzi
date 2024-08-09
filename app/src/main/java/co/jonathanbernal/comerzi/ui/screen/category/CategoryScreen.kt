package co.jonathanbernal.comerzi.ui.screen.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme
import co.jonathanbernal.comerzi.viewModels.CategoryViewModel

@Composable
fun CategoryScreen(categoryViewModel: CategoryViewModel) {
    categoryViewModel.newCategoryName("")
    AddCategoryBox(categoryViewModel)
}

@Composable
fun AddCategoryBox(categoryViewModel: CategoryViewModel) {
    Row(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            Modifier.fillMaxSize()
        ) {
            val categoryValue by categoryViewModel.category.collectAsState()
            val (field, button) = createRefs()
            CustomTextField(
                modifier = Modifier
                    .padding(PaddingValues(16.dp, 16.dp))
                    .constrainAs(field) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                textFieldValue = categoryValue,
                label = "Nombre de la categoria",
                onValueChange = { newValue ->
                    categoryViewModel.newCategoryName(newValue)
                }
            )
            Button(
                modifier = Modifier
                    .constrainAs(button) {
                        start.linkTo(field.end)
                        top.linkTo(field.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(field.bottom)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComerziTheme {
        // CategoryScreen(categoryViewModel)
    }
}