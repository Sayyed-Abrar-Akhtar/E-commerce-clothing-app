package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.response.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {

    @GET("/api/category/")
    suspend fun getCategory(): Response<CategoryResponse>
}