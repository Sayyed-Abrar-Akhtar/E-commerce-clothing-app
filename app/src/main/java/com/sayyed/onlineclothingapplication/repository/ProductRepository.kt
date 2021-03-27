package com.sayyed.onlineclothingapplication.repository


import com.sayyed.onlineclothingapplication.api.ApiRequest
import com.sayyed.onlineclothingapplication.api.ProductApi
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.response.ProductDetailResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse

class ProductRepository(private val productDAO: ProductDAO) : ApiRequest() {

    private val productApi = ServiceBuilder.buildService(ProductApi::class.java)

    suspend fun getProductsOfCategory(category: String): ProductResponse {
        return apiRequest {
            productApi.getProductsOfCategory(category)
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
    fun retrieveCategorizedProductsFromRoom(category: String) = productDAO.retrieveProductsOfCategory(category)

}