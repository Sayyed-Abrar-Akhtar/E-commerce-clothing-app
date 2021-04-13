package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.CategoryApi
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.UploadApi
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.response.*
import okhttp3.MultipartBody

class CategoryRepository(private val categoryDAO: CategoryDAO): ApiRequest() {
    private val categoryApi = ServiceBuilder.buildService(CategoryApi::class.java)
    private val uploadApi = ServiceBuilder.buildService(UploadApi::class.java)

    suspend fun uploadImage(body: MultipartBody.Part): UploadResponse {
        return apiRequest {
            uploadApi.uploadImage(body)
        }
    }

    suspend fun createCategory(
        token: String,
        name: String,
        image: String,
    ): CategoryDetailResponse {
        return apiRequest {
            categoryApi.createCategory(token, name, image)
        }
    }


    suspend fun updateCategory(
        token: String,
        id: String,
        name: String,
        image: String,
    ): CategoryDetailResponse {
        return apiRequest {
            categoryApi.updateCategory(token, id, name, image)
        }
    }


    suspend fun getCategory(): CategoryResponse {
        return apiRequest {
            categoryApi.getCategory()
        }
    }

    suspend fun getCategoryName(): CategoryNameResponse {
        return apiRequest {
            categoryApi.getCategoryName()
        }
    }

    suspend fun getCategoryId(name: String): CategoryIdResponse {
        return apiRequest {
            categoryApi.getCategoryId(name)
        }
    }

    suspend fun deleteCategory(token: String, id: String): DeleteResponse {
        return apiRequest {
            categoryApi.deleteCategory(token, id)
        }
    }

    suspend fun insertCategoryToRoom(category: Category) {
        categoryDAO.createCategory(category)
    }

    suspend fun deleteCategoryFromRoom(id: String) {
        categoryDAO.deleteCategory(id)
    }

    val retrieveCategoryFromRoom = categoryDAO.retrieveCategory()

}