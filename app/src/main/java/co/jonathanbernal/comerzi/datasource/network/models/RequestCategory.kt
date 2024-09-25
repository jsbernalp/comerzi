package co.jonathanbernal.comerzi.datasource.network.models

import com.google.gson.annotations.SerializedName

data class RequestCategory(
    @SerializedName("categoryName") val categoryName: String
)

data class ResponseCategory(
    @SerializedName("id") val id: Int,
    @SerializedName("categoryName") val categoryName: String
)