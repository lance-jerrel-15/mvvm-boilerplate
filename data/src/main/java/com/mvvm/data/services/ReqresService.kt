package com.mvvm.data.services

import com.mvvm.data.responses.LoginResponse
import com.mvvm.data.responses.UserResponse
import com.mvvm.data.responses.UsersResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ReqresService {

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("/api/users")
    suspend fun getUsers(
        @Query("page") page: Int
    ): Response<UsersResponse>

    @GET("/api/users")
    suspend fun getPaging3Users(
        @Query("page") page: Int
    ): Response<UsersResponse>

    @GET("/api/users/{userId}")
    suspend fun getUser(
        @Path("userId") id: Int
    ): Response<UserResponse>
}
