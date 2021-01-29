package com.sayyed.onlineclothingapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.dao.UserDAO

@Database(
        entities = [(User::class)],
        version = 1,
        exportSchema = false
)

abstract class UserDB: RoomDatabase() {
    abstract fun getUserDao() : UserDAO

    companion object{


        @Volatile
        private var instance : UserDB? = null

        fun getInstance(context: Context) : UserDB {
            if (instance == null) {
                synchronized(UserDB::class) {
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        UserDB::class.java,
                        "UserDatabase.db"
                ).build()
    }
}

