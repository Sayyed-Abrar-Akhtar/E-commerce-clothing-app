package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.CategoryApi
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import java.util.*

class CategoryRepository(private val categoryDAO: CategoryDAO): ApiRequest() {
    private val categoryApi = ServiceBuilder.buildService(CategoryApi::class.java)


    suspend fun getCategory(): CategoryResponse {
        return apiRequest {
            categoryApi.getCategory()
        }
    }

    suspend fun insertCategoryToRoom(category: Category) {
        categoryDAO.createCategory(category)
    }

    val retrieveCategoryFromRoom = categoryDAO.retrieveCategory()
}