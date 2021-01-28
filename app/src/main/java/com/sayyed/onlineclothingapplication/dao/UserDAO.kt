package com.sayyed.onlineclothingapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sayyed.onlineclothingapplication.entities.User

@Dao
interface UserDAO {

    // Create new USER
    @Insert
    suspend fun insertUser(user: User) {

    }

    @Query("select * from User where username = (:username) and password= (:password)")
    suspend fun isUserValid(username: String, password: String): User


}