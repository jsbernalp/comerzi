package co.jonathanbernal.comerzi.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.jonathanbernal.comerzi.ui.screen.category.CategoryScreen
import co.jonathanbernal.comerzi.ui.theme.ComerziTheme
import co.jonathanbernal.comerzi.viewModels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComerziTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { _ ->
                        NavController(categoryViewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun NavController(categoryViewModel: CategoryViewModel, navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeContent(navController)
            }
            composable("category") {
                CategoryScreen(categoryViewModel)
            }
        }
    }
}

@Composable
private fun HomeContent(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            Greeting(this, navController)
        }
    }
}

@Composable
fun Greeting(
    parentConstraint: ConstraintLayoutScope,
    navController: NavHostController
) {
    parentConstraint.apply {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { navController.navigate("category") },
            ) {
                Text(text = "Administrar Categorias".trimIndent())
            }
        }
    }
}