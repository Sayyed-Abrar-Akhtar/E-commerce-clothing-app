package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.CategoryApi
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.response.CategoryResponse

class CategoryRepository: ApiRequest() {
    private val categoryApi = ServiceBuilder.buildService(CategoryApi::class.java)


    suspend fun getCategory(): CategoryResponse {
        return apiRequest {
            categoryApi.getCategory()
        }
    }
}