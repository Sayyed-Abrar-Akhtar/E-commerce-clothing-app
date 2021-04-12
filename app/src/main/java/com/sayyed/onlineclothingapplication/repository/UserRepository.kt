package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.UploadApi
import com.sayyed.onlineclothingapplication.api.UserApi
import com.sayyed.onlineclothingapplication.response.UploadResponse
import com.sayyed.onlineclothingapplication.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserApi::class.java)
    private val uploadApi = ServiceBuilder.buildService(UploadApi::class.java)

    suspend fun uploadImage(body: MultipartBody.Part): UploadResponse {
        return apiRequest {
            uploadApi.uploadImage(body)
        }
    }

    suspend fun authLogin(email: String, password: String): UserResponse {
        return apiRequest {
            userApi.authLogin(email, password)
        }
    }

    suspend fun newAccount(
            firstName: String,
            lastName: String,
            image: String,
            contact: String,
            username: String,
            email: String,
            password: String,
    ): UserResponse {
        return apiRequest {
            userApi.newAccount(firstName, lastName, image, contact, username, email, password)
        }
    }

    suspend fun updateUser(
            token: String,
            firstName: String,
            lastName: String,
            contact: String,
            username: String,
            email: String,
            password: String,
            image: String
    ): UserResponse {
        return apiRequest {
            userApi.updateUser(
                token,
                firstName,
                lastName,
                contact,
                username,
                email,
                password,
                image,
            )
        }
    }


}