package com.versec.versecko.data.datasource.local

import com.versec.versecko.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    fun getOwnUser () : Flow<UserEntity>
    fun getUserList (status : Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser (userEntity: UserEntity)
    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)

    suspend fun checkLoungeCount (status: Int) : Int


    suspend fun getAllUser () : MutableList<UserEntity>


    fun saveDuplicateUser (uid : String)



}