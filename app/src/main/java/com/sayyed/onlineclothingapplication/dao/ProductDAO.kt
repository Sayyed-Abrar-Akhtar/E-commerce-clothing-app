package com.sayyed.onlineclothingapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sayyed.onlineclothingapplication.entities.Product


@Dao
interface ProductDAO {

    @Insert
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM Product")
    fun getProducts(): LiveData<List<Product>>

}