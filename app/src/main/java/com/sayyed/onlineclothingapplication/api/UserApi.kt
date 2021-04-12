package com.sayyed.onlineclothingapplication.api


import com.sayyed.onlineclothingapplication.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    //Register User
    @FormUrlEncoded
    @POST("/api/users/")
    suspend fun newAccount(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("image") image: String,
            @Field("contact") contact: String,
            @Field("username") username: String,
            @Field("email") email: String,
            @Field("password") password: String,
    ): Response<UserResponse>


    // Login
    @FormUrlEncoded
    @POST("/api/users/login")
    suspend fun authLogin(
            @Field("email") email :String,
            @Field("password") password :String
    ): Response<UserResponse>


    // Update
    @FormUrlEncoded
    @PUT("/api/users/profile")
    suspend fun updateUser(
            @Header("Authorization") token: String,
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("contact") contact: String,
            @Field("username") username: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("image") image: String,
    ): Response<UserResponse>


    @PUT("/api/users/{id}")
    suspend fun deleteUser(
            @Header("Authorization") token: String,
            @Path("id") id: String,
    ): Response<UserResponse>
}