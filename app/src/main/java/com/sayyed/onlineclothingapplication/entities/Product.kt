package com.sayyed.onlineclothingapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_data_table")
data class Product (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "product_title")
    val productTitle: String,

    @ColumnInfo(name ="product_price")
    val productPrice: Double,

    @ColumnInfo(name = "product_description")
    val productDescription: String,

    @ColumnInfo(name ="product_color")
    val productColor: String,

    @ColumnInfo(name = "product_size")
    val productSize: String,

    @ColumnInfo(name = "product_image")
    val productImage: String
)