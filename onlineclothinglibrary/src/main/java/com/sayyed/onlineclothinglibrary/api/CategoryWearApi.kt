package com.sayyed.onlineclothinglibrary.api

import com.sayyed.onlineclothingapplication.response.*
import retrofit2.Response
import retrofit2.http.*

interface CategoryWearApi {

    @GET("/api/category/")
    suspend fun getCategory(): Response<CategoryWearResponse>

}