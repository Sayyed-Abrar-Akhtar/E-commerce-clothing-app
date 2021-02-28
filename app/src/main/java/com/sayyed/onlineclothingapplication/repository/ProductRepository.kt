package com.sayyed.onlineclothingapplication.repository

import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.entities.Product

class ProductRepository(private val productDAO: ProductDAO) {

    val products = productDAO.getProducts()

    suspend fun insert(product: Product) {
        productDAO.insertProduct(product)
    }

    suspend fun update(product: Product) {
        productDAO.updateProduct(product)
    }

    suspend fun delete(product: Product) {
        productDAO.deleteProduct(product)
    }

    suspend fun deleteAllProduct(product: Product) {
        productDAO.deleteAllProduct()
    }
}