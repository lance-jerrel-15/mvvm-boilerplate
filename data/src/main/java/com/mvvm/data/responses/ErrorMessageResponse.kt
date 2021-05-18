package com.mvvm.data.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorMessageResponse(
    @Json(name = "error")
    val message: String = ""
)
