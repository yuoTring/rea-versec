package com.versec.versecko.data.datasource.remote

import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    fun getOwnUser () : Flow<UserEntity>
    fun getLoungeUserList (status : Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser (userEntity: UserEntity)

    fun signIn (credential: PhoneAuthCredential) : Flow<Results<Int>>
    //suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>
}