package co.jonathanbernal.comerzi.network.services

import co.jonathanbernal.comerzi.network.models.RequestCategory
import co.jonathanbernal.comerzi.network.models.ResponseCategory
import retrofit2.http.Body
import retrofit2.http.POST

interface CategoryServices {

    @POST("categories")
    suspend fun addCategory(
        @Body category: RequestCategory
    ): ResponseCategory


}