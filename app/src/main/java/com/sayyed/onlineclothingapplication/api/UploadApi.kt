package com.sayyed.onlineclothingapplication.api

import com.sayyed.onlineclothingapplication.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Part

interface UploadApi {

    @Multipart
    @POST("/api/upload/")
    suspend fun uploadImage(@Part image: MultipartBody.Part):Response<UploadResponse>

}