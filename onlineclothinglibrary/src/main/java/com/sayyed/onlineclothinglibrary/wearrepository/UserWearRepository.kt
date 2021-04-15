package com.sayyed.onlineclothinglibrary.wearrepository

import com.sayyed.onlineclothingapplication.response.UserWearResponse
import com.sayyed.onlineclothinglibrary.api.ServiceBuilder
import com.sayyed.onlineclothinglibrary.api.UserWearApi


import com.sayyed.onlineclothinglibrary.api.ApiRequest


class UserWearRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserWearApi::class.java)



    suspend fun authLogin(email: String, password: String): UserWearResponse {
        return apiRequest {
            userApi.authLogin(email, password)
        }
    }







}