package co.jonathanbernal.comerzi.datasource.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWIthProducts(
    @Embedded val category: CategoryTable,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "categoryId"
    )
    val products: List<ProductTable>
)