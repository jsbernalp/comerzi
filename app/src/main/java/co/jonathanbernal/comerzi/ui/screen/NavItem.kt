package co.jonathanbernal.comerzi.ui.screen

import co.jonathanbernal.comerzi.R

sealed class NavItem(
    val title: Int,
    val icon: Int,
    val route: String
) {
    data object Product : NavItem(R.string.nav_product_item, R.drawable.baseline_diamond_24, "product")
    data object Category :
        NavItem(R.string.nav_category_item, R.drawable.baseline_category_24, "category")
}