package com.sayyed.onlineclothingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Category (
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @PrimaryKey()
        @SerializedName("image")
        val image: String
        )
