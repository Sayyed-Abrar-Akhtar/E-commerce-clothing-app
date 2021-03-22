package com.sayyed.onlineclothingapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.utils.Converters


@Database(
        entities = [Product::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)

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

