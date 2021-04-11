package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.UserApi
import com.sayyed.onlineclothingapplication.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserApi::class.java)


    suspend fun newAccount(
            firstName: RequestBody,
            lastName: RequestBody,
            contact: RequestBody,
            username: RequestBody,
            email: RequestBody,
            password: RequestBody,
            body: MultipartBody.Part
    ): UserResponse {
        return apiRequest {
            userApi.newAccount(firstName, lastName, contact, username, email, password, body)
        }
    }


    suspend fun authLogin(email: String, password: String): UserResponse {
        return apiRequest {
            userApi.authLogin(email, password)
        }
    }


}