package com.versec.domain.repository

import com.versec.domain.model.UserModel

interface UserRepositoryDepre {

    //suspend fun getUser(uid : String) : User
    suspend fun getAllUser () : MutableList<UserModel>
    suspend fun insertUser (userModel: UserModel)

}