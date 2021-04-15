package com.sayyed.onlineclothingapplication.api


import com.sayyed.onlineclothingapplication.response.DeleteResponse
import com.sayyed.onlineclothingapplication.response.UserDetailsResponse
import com.sayyed.onlineclothingapplication.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    //Register
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

    @GET("/api/users/")
    suspend fun allUsers(
        @Header("Authorization") token: String,
    ): Response<UserDetailsResponse>

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


    @DELETE("/api/users/{id}")
    suspend fun deleteUser(
            @Header("Authorization") token: String,
            @Path("id") id: String,
    ): Response<DeleteResponse>



}