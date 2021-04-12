package com.sayyed.onlineclothingapplication.repository


import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.ProductApi
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.api.UploadApi
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.response.ProductDetailResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.response.UploadResponse
import okhttp3.MultipartBody

class ProductRepository(private val productDAO: ProductDAO) : ApiRequest() {

    private val productApi = ServiceBuilder.buildService(ProductApi::class.java)
    private val uploadApi = ServiceBuilder.buildService(UploadApi::class.java)

    suspend fun uploadImage(body: MultipartBody.Part): UploadResponse {
        return apiRequest {
            uploadApi.uploadImage(body)
        }
    }


    suspend fun addProduct(token: String): ProductDetailResponse {
        return apiRequest {
            productApi.addProduct(token)
        }
    }

    suspend fun updateProduct(
        token: String,
        id: String,
        name: String,
        price: Int,
        description: String,
        image: String,
        brand: String,
        countInStock: Int
    ): ProductDetailResponse {
        return apiRequest {
            productApi.updateProduct(token, id, name, price, description, image, brand, countInStock)
        }
    }

    suspend fun getProductsOfCategory(id: String): ProductResponse {
        return apiRequest {
            productApi.getProductsOfCategory(id)
        }
    }

    suspend fun getProductById(id: String): ProductDetailResponse {
        return apiRequest {
            productApi.getProductById(id)
        }
    }

    suspend fun getAllProducts(): ProductResponse {
        return apiRequest {
            productApi.getAllProducts()
        }
    }

    suspend fun insertProductIntoRoom(product: Product) {
        productDAO.createProduct(product)
    }


    suspend fun updateProductsInRoom(product: Product) {
        productDAO.updateProduct(product)
    }


    suspend fun deleteProductsFromRoom() {
        productDAO.deleteAllProduct()
    }

    val retrieveProductsFromRoom = productDAO.retrieveProducts()
    fun retrieveProductByIdFromRoom(id:String) = productDAO.retrieveProductById(id)
    fun retrieveCategorizedProductsFromRoom(category: String) = productDAO.retrieveProductsOfCategory(category)

}