package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.UserApi
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.response.LoginResponse

class UserRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserApi::class.java)



}