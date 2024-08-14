package co.jonathanbernal.comerzi.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.jonathanbernal.comerzi.ui.screen.category.CategoryScreen
import co.jonathanbernal.comerzi.ui.screen.product.AddProductScreen
import co.jonathanbernal.comerzi.ui.screen.product.ProductScreen
import co.jonathanbernal.comerzi.ui.theme.Purple40
import co.jonathanbernal.comerzi.viewModels.category.CategoryViewModel
import co.jonathanbernal.comerzi.viewModels.product.AddProductViewModel
import co.jonathanbernal.comerzi.viewModels.product.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val addProductViewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewContainer()
        }
    }

    @Composable
    private fun ViewContainer() {
        val navController = rememberNavController()
        Scaffold(
            content = { innerPadding ->
                NavController(
                    addProductViewModel,
                    productViewModel,
                    categoryViewModel,
                    navController,
                    innerPadding
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun Toolbar() {
        TopAppBar(
            title = {
                Text(text = "Comerzi")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Purple40,
                titleContentColor = Color.White
            )
        )
    }
}

@Composable
fun NavController(
    addProductViewModel: AddProductViewModel,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(navController = navController, startDestination = NavItem.Home.route) {
        composable(NavItem.Home.route) {
            ProductScreen(
                productViewModel = productViewModel,
                navController = navController,
                innerPadding
            )
        }
        composable(NavItem.Category.route) {
            CategoryScreen(categoryViewModel, innerPadding)
        }
        composable("addProduct") {
            AddProductScreen(addProductViewModel, navController, innerPadding)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(NavItem.Home, NavItem.Category)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    BottomAppBar {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(text = item.title) },
                    icon = {
                        Icon(painterResource(id = item.icon), contentDescription = item.title)
                    })
            }
        }
    }
}
