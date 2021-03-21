package com.sayyed.onlineclothingapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sayyed.onlineclothingapplication.models.Category


@Dao
interface CategoryDAO {

    @Query("SELECT * FROM Category ORDER BY id LIMIT 10")
    fun retrieveCategory():LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun createCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)
}