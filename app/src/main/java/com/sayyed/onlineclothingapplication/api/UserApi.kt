package com.sayyed.onlineclothingapplication.api


import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.response.LoginResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    //Register User
    @POST("auth/register")
    suspend fun registerUser(
        @Body user: User
    ): Response<LoginResponse>

    // Login
    @FormUrlEncoded
    @POST ("auth/login")
    suspend fun checkUser(
        @Field ("username") username :String,
        @Field ("password") password :String
    ): Response<LoginResponse>
}