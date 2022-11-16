package com.versec.versecko.data.datasource.local

import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    fun getOwnUser () : Flow<UserEntity>
    suspend fun insertUser (userEntity: UserEntity) : Response<Int>
    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)

    fun saveDuplicateUser (uid : String)
    fun getDuplicateUsers () : MutableList<String>?

    fun setGenderValue (genderValue : String)
    fun setAgeRange (min : Int, max : Int)
    fun setDistance (distance : Int)

    fun getGenderValue () : String?
    fun getAgeRange() : List<Int>?
    fun getDistance() : Int?

    fun setTimestamp ()
    fun getTimestamp () : Long

    fun setMatchingNotification (on : Boolean)
    fun setLikedNotification (on : Boolean)
    fun setChatNotification (on: Boolean)
    fun setKnockNotification (on: Boolean)
    fun setMarketingNotification (on: Boolean)

    fun getMatchingNotification () : Boolean
    fun getLikedNotification () : Boolean
    fun getChatNotification () : Boolean
    fun getKnockNotification () : Boolean
    fun getMarketingNotification () : Boolean

    suspend fun updateDeletedAt (activate : Boolean) : Response<Int>
}