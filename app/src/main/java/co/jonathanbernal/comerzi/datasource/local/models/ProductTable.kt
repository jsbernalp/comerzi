package co.jonathanbernal.comerzi.datasource.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productTable")
data class ProductTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ean: String,
    val price: Double,
)