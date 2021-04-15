package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothinglibrary.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.UploadApi
import com.sayyed.onlineclothingapplication.api.UserApi
import com.sayyed.onlineclothingapplication.response.DeleteResponse
import com.sayyed.onlineclothingapplication.response.UploadResponse
import com.sayyed.onlineclothingapplication.response.UserDetailsResponse
import com.sayyed.onlineclothingapplication.response.UserWearResponse
import com.sayyed.onlineclothinglibrary.api.ServiceBuilder
import okhttp3.MultipartBody

class UserRepository: ApiRequest() {

    private val userApi = ServiceBuilder.buildService(UserApi::class.java)
    private val uploadApi = ServiceBuilder.buildService(UploadApi::class.java)

    suspend fun uploadImage(body: MultipartBody.Part): UploadResponse {
        return apiRequest {
            uploadApi.uploadImage(body)
        }
    }

    suspend fun authLogin(email: String, password: String): UserWearResponse {
        return apiRequest {
            userApi.authLogin(email, password)
        }
    }

    suspend fun allUsers(token: String): UserDetailsResponse {
        return apiRequest {
            userApi.allUsers(token)
        }
    }

    suspend fun deleteUser(token: String, id:String): DeleteResponse {
        return apiRequest {
            userApi.deleteUser(token, id)
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
    ): UserWearResponse {
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
    ): UserWearResponse {
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