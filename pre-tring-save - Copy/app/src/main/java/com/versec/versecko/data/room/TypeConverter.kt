package com.versec.versecko.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TypeConverter {

    @TypeConverter
    fun mutableListToJson(value:MutableList<String>) : String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToMutableList (value : String) : MutableList<String> {
        return Gson().fromJson(value, Array<String>::class.java).toMutableList()
    }

    @TypeConverter
    fun mutableMapToJson (value: MutableMap<Int, String>) : String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToMutableMap (value: String) : MutableMap<Int, String> {

        val gson = Gson()
        val mapType = object : TypeToken<MutableMap<Int,String>>() {}.type

        return Gson().fromJson(value, mapType)
    }
}