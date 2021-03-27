package com.sayyed.onlineclothingapplication.api


import com.sayyed.onlineclothingapplication.response.ProductDetailResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("/api/category/{category}")
    suspend fun getProductsOfCategory(@Path("category") category: String): Response<ProductResponse>

    @GET("/api/products/")
    suspend fun getAllProducts():Response<ProductResponse>

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id", encoded = false) id: String): Response<ProductDetailResponse>


}