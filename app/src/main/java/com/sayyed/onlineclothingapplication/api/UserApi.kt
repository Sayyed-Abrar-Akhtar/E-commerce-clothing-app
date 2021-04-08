package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    //Register User
    @POST("user/new")
    suspend fun newAccount(
            @Body user: User
    ): Response<LoginResponse>

    // Login
    @FormUrlEncoded
    @POST("user/login")
    suspend fun authLogin(
            @Field("email") email :String,
            @Field("password") password :String
    ): Response<LoginResponse>
}