package com.sayyed.onlineclothingapplication.api


import com.sayyed.onlineclothingapplication.response.ProductDetailResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {

    @GET("/api/products/category/{id}")
    suspend fun getProductsOfCategory(@Path("id") id: String): Response<ProductResponse>

    @GET("/api/products/")
    suspend fun getAllProducts():Response<ProductResponse>

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id", encoded = false) id: String): Response<ProductDetailResponse>

    @POST("/api/products/")
    suspend fun addProduct(
            @Header("Authorization") token: String
    ):Response<ProductDetailResponse>


    @FormUrlEncoded
    @PUT("/api/products/{id}")
    suspend fun updateProduct(
            @Header("Authorization") token: String,
            @Path("id", encoded = false) id: String,
            @Field("name") name: String,
            @Field("price") price: Int,
            @Field("description") description: String,
            @Field("image") image: String,
            @Field("brand") brand: String,
            @Field("countInStock") countInStock: Int,
    ): Response<ProductDetailResponse>


}