package co.jonathanbernal.comerzi.datasource.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date

@Entity(tableName = "categoryTable")
data class CategoryTable(
    @PrimaryKey(autoGenerate = true)
    val idCategory: Int = 0,
    val categoryName: String,
    val date: Date = Date.from(Instant.now())
)