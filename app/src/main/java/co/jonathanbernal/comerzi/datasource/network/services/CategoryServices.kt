package co.jonathanbernal.comerzi.datasource.network.services

import co.jonathanbernal.comerzi.datasource.network.models.RequestCategory
import co.jonathanbernal.comerzi.datasource.network.models.ResponseCategory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CategoryServices {

    @POST("categories")
    suspend fun addCategory(
        @Body category: RequestCategory
    ): Response<Unit>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("categories")
    suspend fun getCategories(): Response<List<ResponseCategory>>
}