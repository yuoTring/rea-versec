package com.versec.versecko.data.datasource.local

import com.versec.versecko.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    fun getOwnUser () : Flow<UserEntity>
    suspend fun insertUser (userEntity: UserEntity)
    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)

    fun saveDuplicateUser (uid : String)
    fun getDuplicateUsers () : MutableList<String>?

    fun setGenderValue (genderValue : String)
    fun setAgeRange (min : Int, max : Int)
    fun setDistance (distance : Int)

    fun getGenderValue () : String?
    fun getAgeRange() : List<Int>?
    fun getDistance() : Int?

    fun setLikedCounter (count : Int)
    fun setMatchingCounter (count: Int)

    fun getLikedCounter () : Int?
    fun getMatchingCounter () : Int?



}