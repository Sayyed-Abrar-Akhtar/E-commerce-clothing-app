package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.response.*
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {

    @GET("/api/category/")
    suspend fun getCategory(): Response<CategoryResponse>

    @GET("/api/category/name")
    suspend fun getCategoryName(): Response<CategoryNameResponse>

    @GET("/api/category/name/{name}")
    suspend fun getCategoryId(@Path("name") name: String): Response<CategoryIdResponse>

    @FormUrlEncoded
    @POST("/api/category/")
    suspend fun createCategory(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("image") image: String,
    ): Response<CategoryDetailResponse>

    @FormUrlEncoded
    @PUT("/api/category/{id}")
    suspend fun updateCategory(
        @Header("Authorization") token: String,
        @Path("id", encoded = false) id: String,
        @Field("name") name: String,
        @Field("image") image: String,
    ): Response<CategoryDetailResponse>

    @DELETE("/api/category/{id}")
    suspend fun deleteCategory(
        @Header("Authorization") token: String,
        @Path("id", encoded = false) id: String,
    ):Response<DeleteResponse>
}