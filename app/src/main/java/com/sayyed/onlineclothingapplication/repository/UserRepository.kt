package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.UserApi
import com.sayyed.onlineclothingapplication.response.UserResponse
import retrofit2.Response

class UserRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserApi::class.java)

    suspend fun authLogin(email: String, password: String): UserResponse {
        return apiRequest {
            userApi.authLogin(email, password)
        }
    }


}