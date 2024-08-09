package co.jonathanbernal.comerzi.di

import co.jonathanbernal.comerzi.network.services.CategoryServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CategoryModule {

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryServices {
        return retrofit.create(CategoryServices::class.java)
    }

}