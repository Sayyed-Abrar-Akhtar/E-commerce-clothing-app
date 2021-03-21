package com.sayyed.onlineclothingapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.models.Category

@Database(
    entities = [Category::class],
    version = 1,
    exportSchema = false
)
abstract class CategoryDB: RoomDatabase() {

    abstract val categoryDAO: CategoryDAO

    companion object {

        @Volatile
        private var INSTANCE: CategoryDB? = null

        fun getInstance(context: Context): CategoryDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CategoryDB::class.java,
                        "category_database"
                    ).build()
                }
                return instance
            }
        }
    }

}