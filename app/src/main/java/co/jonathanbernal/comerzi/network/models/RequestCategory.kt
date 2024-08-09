package co.jonathanbernal.comerzi.network.models

import com.google.gson.annotations.SerializedName

data class RequestCategory(
    @SerializedName("categoryName") val categoryName: String
)

data class ResponseCategory(
    @SerializedName("id") val id: String,
    @SerializedName("categoryName") val categoryName: String
)