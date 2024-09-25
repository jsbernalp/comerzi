package co.jonathanbernal.comerzi.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.jonathanbernal.comerzi.ui.screen.camera.CameraPreview
import co.jonathanbernal.comerzi.ui.screen.category.CategoryScreen
import co.jonathanbernal.comerzi.ui.screen.product.AddProductScreen
import co.jonathanbernal.comerzi.ui.screen.product.ProductScreen
import co.jonathanbernal.comerzi.utils.orEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewContainer {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    @Composable
    private fun ViewContainer(openPermissionSettings: () -> Unit) {
        val navController = rememberNavController()
        var hideNavigationBar by remember {
            mutableStateOf(false)
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                NavController(navController, innerPadding, {
                    hideNavigationBar = it
                }, {
                    openPermissionSettings()
                })
            },
            bottomBar = {
                if (hideNavigationBar.not()) {
                    BottomNavigationBar(navController)
                }
            }
        )
    }
}

@Composable
fun NavController(
    navController: NavHostController,
    innerPadding: PaddingValues,
    hideNavigationBar: (Boolean) -> Unit,
    openPermissionSettings: () -> Unit
) {
    NavHost(navController = navController, startDestination = NavItem.Product.route) {
        composable(NavItem.Product.route) {
            hideNavigationBar(false)
            ProductScreen(innerPadding) { _ ->
                navigateTo(navController, "addProduct")
            }
        }
        composable(NavItem.Category.route) {
            hideNavigationBar(false)
            CategoryScreen(innerPadding)
        }
        composable(
            "addProduct?uri={uri}",
            arguments = listOf(navArgument("uri") { defaultValue = "" })
        ) { backStackEntry ->
            hideNavigationBar(true)
            AddProductScreen(
                navController,
                innerPadding,
                backStackEntry.arguments?.getString("uri").orEmpty()
            ) {
                navigateTo(navController, "takePhoto")
            }
        }
        composable("takePhoto") {
            hideNavigationBar(true)
            CameraPreview(innerPadding = innerPadding, { uri ->
                navController.navigate("addProduct?uri=${uri}") {
                    launchSingleTop = true
                    popUpTo("takePhoto") {
                        inclusive = true
                    }
                }
            }, {
                openPermissionSettings()
            })
        }
    }
}

private fun navigateTo(navController: NavHostController, route: String) {
    navController.navigate(route) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    navigate(route) {
        popUpTo(route) {
            saveState = true
        }
        launchSingleTop = true
    }

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(NavItem.Product, NavItem.Category)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    BottomAppBar(modifier = Modifier.wrapContentSize()) {
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
                    label = { Text(text = stringResource(item.title)) },
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = stringResource(item.title)
                        )
                    })
            }
        }
    }
}
