package com.versec.versecko.data.datasource.local

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.room.AppDatabase
import com.versec.versecko.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserLocalDataSourceImpl(

    private val appDatabase: AppDatabase,
    private val userPreferences: SharedPreferences,
    private val filterPreferences: SharedPreferences,
    private val loungePreferences: SharedPreferences,
    private val settingPreferences: SharedPreferences


    ) : UserLocalDataSource {

    companion object {

        const val SUCCESS = 1
    }

    override fun getOwnUser(): Flow<UserEntity> {

        return appDatabase.userEntityDao().getOwnUser()

    }

    override suspend fun insertUser(userEntity: UserEntity): Response<Int> {

        try {

            appDatabase.userEntityDao().insertUser(userEntity)

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }


    override suspend fun updateUriList(uriMap: MutableMap<String, String>, status: Int) {

        appDatabase.userEntityDao().updateUriList(uriMap, status)
    }

    override fun saveDuplicateUser(uid: String) {

        val editor = userPreferences.edit()

        if (userPreferences.contains("duplicateUsers")) {

            var json =
                userPreferences.getString("duplicateUsers", null)

            val type = object : TypeToken<MutableList<String>>() {}.type

            val list : MutableList<String>
                = Gson().fromJson(json, type)

            list.add(uid)

            json = Gson().toJson(list)

            editor.putString("duplicateUsers", json)
            editor.apply()

        } else {

            val list = mutableListOf<String>()

            if (!list.contains(uid))
                list.add(uid)

            val json = Gson().toJson(list)

            editor.putString("duplicateUsers", json)
            editor.apply()
        }

    }

    override fun getDuplicateUsers(): MutableList<String>? {

        if (userPreferences.contains("duplicateUsers")) {

            var json =
                userPreferences.getString("duplicateUsers", null)

            val type = object : TypeToken<MutableList<String>>() {}.type

            val list : MutableList<String>
                    = Gson().fromJson(json, type)

            return list

        } else {

            return null
        }
    }


    override fun setGenderValue(genderValue: String) {

        val editor = filterPreferences.edit()

        editor.putString("gender", genderValue)
        editor.apply()
    }

    override fun setAgeRange(min: Int, max: Int) {

        val editor = filterPreferences.edit()

        editor.putInt("ageMin", min)
        editor.putInt("ageMax", max)
        editor.apply()

    }

    override fun setDistance(distance: Int) {

        val editor = filterPreferences.edit()

        editor.putInt("distance", distance)
        editor.apply()
    }

    override fun getGenderValue(): String? {

        if (filterPreferences.contains("gender"))
            return filterPreferences.getString("gender", "both")
        else
            return null
    }

    override fun getAgeRange(): List<Int>? {

        if (filterPreferences.contains("ageMin") && filterPreferences.contains("ageMax")) {

            val temp = mutableListOf<Int>()

            temp.add(0,filterPreferences.getInt("ageMin", 20))
            temp.add(1,filterPreferences.getInt("ageMax", 80))

            return temp.toList()

        } else { return null }
    }

    override fun getDistance(): Int? {
        if (filterPreferences.contains("distance"))
            return filterPreferences.getInt("distance", 20)
        else
            return null
    }

    override fun setLikedCounter(count: Int) {

        val editor = loungePreferences.edit()

        editor.putInt("likedCounter", count)
        editor.apply()
    }

    override fun setMatchingCounter(count: Int) {

        val editor = loungePreferences.edit()

        editor.putInt("matchingCounter", count)
        editor.apply()
    }

    override fun getLikedCounter(): Int? {

        if (loungePreferences.contains("likedCounter"))
            return loungePreferences.getInt("likedCounter",0)
        else
            return null
    }

    override fun getMatchingCounter(): Int? {
        if (loungePreferences.contains("matchingCounter"))
            return loungePreferences.getInt("matchingCounter",0)
        else
            return null    }

    override fun setMatchingNotification(on: Boolean) {

        val editor = settingPreferences.edit()

        editor.putBoolean("MatchingNotification", on)
        editor.apply()
    }

    override fun setLikedNotification(on: Boolean) {

        val editor = settingPreferences.edit()

        editor.putBoolean("LikedNotification", on)
        editor.apply()
    }

    override fun setChatNotification(on: Boolean) {
        val editor = settingPreferences.edit()

        editor.putBoolean("ChatNotification", on)
        editor.apply()
    }

    override fun setKnockNotification(on: Boolean) {
        val editor = settingPreferences.edit()

        editor.putBoolean("KnockNotification", on)
        editor.apply()
    }

    override fun setMarketingNotification(on: Boolean) {
        val editor = settingPreferences.edit()

        editor.putBoolean("MarketingNotification", on)
        editor.apply()
    }

    override fun getMatchingNotification(): Boolean {

        if (settingPreferences.contains("MatchingNotification"))
            return settingPreferences.getBoolean("MatchingNotification", true)
        else
            return true
    }

    override fun getLikedNotification(): Boolean {

        if (settingPreferences.contains("LikedNotification"))
            return settingPreferences.getBoolean("LikedNotification", true)
        else
            return true
    }

    override fun getChatNotification(): Boolean {

        if (settingPreferences.contains("ChatNotification"))
            return settingPreferences.getBoolean("ChatNotification", true)
        else
            return true
    }

    override fun getKnockNotification(): Boolean {

        if (settingPreferences.contains("KnockNotification"))
            return settingPreferences.getBoolean("KnockNotification", true)
        else
            return true
    }

    override fun getMarketingNotification(): Boolean {

        if (settingPreferences.contains("MarketingNotification"))
            return settingPreferences.getBoolean("MarketingNotification",true)
        else
            return true
    }

    override suspend fun updateDeletedAt(activate: Boolean): Response<Int> {

        try {

            if (!activate)
                appDatabase.userEntityDao().updateDeletedAt(Timestamp.now().toDate(),AppContext.uid)
            else
                appDatabase.userEntityDao().updateDeletedAt(null, AppContext.uid)

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }


}

