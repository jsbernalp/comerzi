package co.jonathanbernal.comerzi.datasource.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productTable",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = CategoryTable::class,
            parentColumns = ["idCategory"],
            childColumns = ["categoryId"],
            onDelete = androidx.room.ForeignKey.CASCADE,
            onUpdate = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class ProductTable(
    @PrimaryKey(autoGenerate = true)
    val idProduct: Int = 0,
    val name: String,
    val ean: String,
    val price: Double,
    val photo: String,
    val categoryId: Int,
)