package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.response.LoginResponse
import com.sayyed.onlineclothingapplication.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    //Register User
    @POST("user/new")
    suspend fun newAccount(
            @Body user: User
    ): Response<UserResponse>

    // Login
    @FormUrlEncoded
    @POST("/api/users/login")
    suspend fun authLogin(
            @Field("email") email :String,
            @Field("password") password :String
    ): Response<UserResponse>
}