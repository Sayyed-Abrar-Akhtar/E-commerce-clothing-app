package com.sayyed.onlineclothingapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sayyed.onlineclothingapplication.entities.Product

interface ProductDAO {

    @Insert
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_data_table")
    suspend fun deleteAllProduct()

    @Query("SELECT * FROM product_data_table")
    fun getProducts(): LiveData<List<Product>>

}