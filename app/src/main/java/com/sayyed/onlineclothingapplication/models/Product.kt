package com.sayyed.onlineclothingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class Product (

    @SerializedName("__v")
    val __v: Int,

    @PrimaryKey
    @SerializedName("_id")
    val _id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("brand")
    val brand: String,

    @SerializedName("category")
    val category: Category,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("countInStock")
    val countInStock: Int,

    @SerializedName("numReviews")
    val numReviews: Int,

    @SerializedName("reviews")
    val reviews: ReviewsList,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("user")
    val user: String

)