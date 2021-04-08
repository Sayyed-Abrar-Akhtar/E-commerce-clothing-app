package com.sayyed.onlineclothingapplication.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.Review
import java.lang.reflect.Type
import java.util.*


class Converters {

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Review?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Review?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Review?>?): String? {
        return Gson().toJson(someObjects)
    }

}