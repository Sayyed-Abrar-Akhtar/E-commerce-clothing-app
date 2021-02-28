package com.sayyed.onlineclothingapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sayyed.onlineclothingapplication.entities.Product


@Database(
        entities = [Product::class],
        version = 1,
        exportSchema = false
)
abstract class ProductDB : RoomDatabase() {
}