package com.sayyed.onlineclothinglibrary.api


import com.sayyed.onlineclothingapplication.response.UserWearResponse
import retrofit2.Response
import retrofit2.http.*

interface UserWearApi {
    // Login
    @FormUrlEncoded
    @POST("/api/users/login")
    suspend fun authLogin(
            @Field("email") email :String,
            @Field("password") password :String
    ): Response<UserWearResponse>
}