package com.mvvm.data.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token")
    val token: String = "",
    @Json(name = "error")
    val error: String? = null
)