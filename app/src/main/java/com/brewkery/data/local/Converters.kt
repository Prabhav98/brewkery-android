package com.brewkery.data.local

import androidx.room.TypeConverter
import com.brewkery.data.model.Customizations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromCustomizations(value: Customizations): String = gson.toJson(value)

    @TypeConverter
    fun toCustomizations(value: String): Customizations =
        gson.fromJson(value, Customizations::class.java)
}