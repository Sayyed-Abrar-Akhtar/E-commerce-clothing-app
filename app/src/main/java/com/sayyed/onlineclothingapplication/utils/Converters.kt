package com.sayyed.onlineclothingapplication.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.ReviewsList

class Converters {
    @TypeConverter
    fun categoryToString(category: Category): String = Gson().toJson(category)

    @TypeConverter
    fun stringToCategory(string: String): Category = Gson().fromJson(string, Category::class.java)

    @TypeConverter
    fun reviewsToString(reviewsList: ReviewsList) = Gson().toJson(reviewsList)

    @TypeConverter
    fun stringToReviews(string: String) = Gson().fromJson(string, ReviewsList::class.java)
}