package com.sayyed.onlineclothingapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.dao.UserDAO
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.utils.Converters


@Database(
        entities = [Product::class, Category::class, User::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OnlineClothingDB: RoomDatabase() {
    abstract val productDAO: ProductDAO
    abstract val categoryDAO: CategoryDAO
    abstract val userDAO: UserDAO

    companion object {

        @Volatile
        private var INSTANCE: OnlineClothingDB? = null

        fun getInstance(context: Context): OnlineClothingDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            OnlineClothingDB::class.java,
                            "online_clothing_database"
                    ).build()
                }
                return instance
            }
        }
    }
}