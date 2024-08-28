package co.jonathanbernal.comerzi.ui.models

import java.util.Date

data class Category (
    val name: String,
    val id: Int,
    val date: Date? = null
)