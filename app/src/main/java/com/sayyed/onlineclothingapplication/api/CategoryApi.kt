package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.response.CategoryIdResponse
import com.sayyed.onlineclothingapplication.response.CategoryNameResponse
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApi {

    @GET("/api/category/")
    suspend fun getCategory(): Response<CategoryResponse>

    @GET("/api/category/name")
    suspend fun getCategoryName(): Response<CategoryNameResponse>

    @GET("/api/category/name/{name}")
    suspend fun getCategoryId(@Path("name") name: String): Response<CategoryIdResponse>
}