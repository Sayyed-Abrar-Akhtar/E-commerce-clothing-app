package com.sayyed.onlineclothingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Category (
        @SerializedName("__v")
        val __v: Int,
        @PrimaryKey()
        @SerializedName("_id")
        val _id: String,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("updatedAt")
        val updatedAt: String
)
