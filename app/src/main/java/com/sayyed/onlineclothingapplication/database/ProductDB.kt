package com.sayyed.onlineclothingapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.entities.Product


@Database(
        entities = [Product::class],
        version = 1,
        exportSchema = false
)
abstract class ProductDB : RoomDatabase() {

    abstract val productDAO: ProductDAO

    companion object {

        @Volatile
        private var INSTANCE: ProductDB? = null

        fun getInstance(context: Context): ProductDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ProductDB::class.java,
                            "product_database"
                    ).build()
                }
                return instance
            }
        }
    }
}

