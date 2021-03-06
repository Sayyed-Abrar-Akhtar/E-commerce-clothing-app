package com.sayyed.onlineclothingapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import com.sayyed.onlineclothingapplication.entities.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insertUser(user: User)

}