package co.jonathanbernal.comerzi.di

import android.content.Context
import androidx.room.Room
import co.jonathanbernal.comerzi.datasource.local.ComerziDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ComerziDatabase =
        Room.databaseBuilder(
            context,
            ComerziDatabase::class.java,
            "comerzi_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideProductDao(comerziDatabase: ComerziDatabase) = comerziDatabase.productDao()
}