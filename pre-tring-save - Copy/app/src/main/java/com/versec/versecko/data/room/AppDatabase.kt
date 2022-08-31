package com.versec.versecko.data.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun userEntityDao() : UserEntityDAO

}