package com.mvvm.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class DoggoImageModel(
    @Json(name = "id")
    val id: String? = "",
    @Json(name = "url")
    val url: String? = ""
)
