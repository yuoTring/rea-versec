package com.versec.versecko.data.datasource.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(

    private val appDatabase: AppDatabase,
    private val preferences: SharedPreferences

) : UserLocalDataSource {
    override fun getOwnUser(): Flow<UserEntity> {

        return appDatabase.userEntityDao().getOwnUser()

    }

    override fun getUserList(status: Int): Flow<MutableList<UserEntity>> {

        return appDatabase.userEntityDao().getUserList(status)
    }

    override suspend fun insertUser(userEntity: UserEntity) {

        appDatabase.userEntityDao().insertUser(userEntity)
    }

    override suspend fun updateUriList(uriMap: MutableMap<String, String>, status: Int) {

        appDatabase.userEntityDao().updateUriList(uriMap, status)
    }

    override suspend fun checkLoungeCount(status: Int): Int {
        return appDatabase.userEntityDao().checkLoungeCount(status)
    }

    override suspend fun getAllUser(): MutableList<UserEntity> {

        return appDatabase.userEntityDao().getAllUser()
    }

    override fun saveDuplicateUser(uid: String) {

        val editor = preferences.edit()

        if (preferences.contains("duplicateUsers")) {

            var json =
                preferences.getString("duplicateUsers", null)

            val type = object : TypeToken<MutableList<String>>() {}.type

            val list : MutableList<String>
                = Gson().fromJson(json, type)

            list.add(uid)

            json = Gson().toJson(list)

            editor.putString("duplicateUsers", json)
            editor.apply()

        } else {

            val list = mutableListOf<String>()
            list.add(uid)

            val json = Gson().toJson(list)

            editor.putString("duplicateUsers", json)
            editor.apply()
        }

    }


}

