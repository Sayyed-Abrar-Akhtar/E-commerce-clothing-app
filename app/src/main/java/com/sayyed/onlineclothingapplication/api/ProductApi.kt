package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("/api/category/{category}")
    suspend fun getProductsOfCategory(@Path("category") category: String): Response<ProductResponse>

    @GET("/api/products/")
    suspend fun getAllProducts():Response<ProductResponse>

}