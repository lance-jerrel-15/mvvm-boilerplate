package com.mvvm.data.services

import com.mvvm.data.model.DoggoImageModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DoggoService {

    @GET("v1/images/search")
    suspend fun getDoggoImages(@Query("page") page: Int, @Query("limit") size: Int): List<DoggoImageModel>

}