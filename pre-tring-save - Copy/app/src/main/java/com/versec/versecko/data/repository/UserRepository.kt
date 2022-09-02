package com.versec.versecko.data.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.versec.domain.model.UserModel
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow

interface UserRepository {


    fun getOwnUser_Local () : Flow<UserEntity>
    fun getUserList_Local (status : Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser_Local (userEntity: UserEntity)

    fun getOwnUser_Remote () : Flow<UserEntity>
    fun getLoungeUserList_Remote (status: Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser_Remote(userEntity: UserEntity)

    suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>
    //fun signIn(credential: PhoneAuthCredential) : Flow<Results<Int>>
    suspend fun updateUriList (uriList: MutableList<String>, status: Int)



}