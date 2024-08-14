package co.jonathanbernal.comerzi.ui.screen

import co.jonathanbernal.comerzi.R

sealed class NavItem(
    val title: String,
    val icon: Int,
    val route: String
) {
    data object Home : NavItem("Home", R.drawable.baseline_diamond_24, "home")
    data object Category : NavItem("Category", R.drawable.baseline_category_24, "category")
}