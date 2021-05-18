package com.mvvm.data.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "data")
    val `data`: Data = Data(),
    @Json(name = "support")
    val support: Support = Support(),
    @Json(name = "error")
    val error: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "id")
        val id: Int = 0,
        @Json(name = "email")
        val email: String = "",
        @Json(name = "first_name")
        val firstName: String = "",
        @Json(name = "last_name")
        val lastName: String = "",
        @Json(name = "avatar")
        val avatar: String = ""
    )

    @JsonClass(generateAdapter = true)
    data class Support(
        @Json(name = "url")
        val url: String = "",
        @Json(name = "text")
        val text: String = ""
    )
}