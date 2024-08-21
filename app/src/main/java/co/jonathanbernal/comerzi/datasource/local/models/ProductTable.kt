package co.jonathanbernal.comerzi.datasource.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productTable")
data class ProductTable(

    @PrimaryKey(autoGenerate = true)
    val idProduct: Int = 0,
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    @Embedded val category: CategoryTable
)