package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.UserAPI
import com.sayyed.onlineclothingapplication.api.UserApiRequest
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.response.LoginResponse

class UserRepository: UserApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserAPI::class.java)

    //register user
    suspend fun registerUser(user: User): LoginResponse {
        return apiRequest {
            userApi.registerUser(user)
        }
    }

    //login user
    suspend fun checkUser(username: String, password: String): LoginResponse {
        return apiRequest {
            userApi.checkUser(username, password)
        }
    }

}