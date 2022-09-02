package com.versec.versecko.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson


class TypeConverter {

    @TypeConverter
    fun mutableListToJson(value:MutableList<String>) : String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToMutableList (value : String) : MutableList<String> {
        return Gson().fromJson(value, Array<String>::class.java).toMutableList()
    }
}