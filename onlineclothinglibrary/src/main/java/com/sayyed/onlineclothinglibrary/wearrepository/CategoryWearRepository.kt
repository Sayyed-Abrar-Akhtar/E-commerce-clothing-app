package com.sayyed.onlineclothinglibrary.wearrepository

import com.sayyed.onlineclothingapplication.response.*
import com.sayyed.onlineclothinglibrary.api.ApiRequest
import com.sayyed.onlineclothinglibrary.api.CategoryWearApi
import com.sayyed.onlineclothinglibrary.api.ServiceBuilder

class CategoryWearRepository(): ApiRequest() {
    private val categoryApi = ServiceBuilder.buildService(CategoryWearApi::class.java)

    suspend fun getCategory(): CategoryWearResponse {
        return apiRequest {
            categoryApi.getCategory()
        }
    }
}