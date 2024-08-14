package co.jonathanbernal.comerzi.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.jonathanbernal.comerzi.datasource.local.dao.CategoryDatabaseDao
import co.jonathanbernal.comerzi.datasource.local.models.CategoryTable
import co.jonathanbernal.comerzi.utils.Converters

@Database(entities = [CategoryTable::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ComerziDatabase : RoomDatabase() {
    abstract fun categoryDatabaseDao(): CategoryDatabaseDao
}