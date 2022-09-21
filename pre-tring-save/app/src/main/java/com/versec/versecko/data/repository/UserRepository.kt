package com.versec.versecko.data.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.domain.model.UserModel
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {


    fun getOwnUser_Local () : Flow<UserEntity>
    fun getUserList_Local (status : Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser_Local (userEntity: UserEntity)

    suspend fun checkLocalLoungeCount (status: Int) : Int

    suspend fun getAllUser () : MutableList<UserEntity>


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun getOwnUser_Remote () : Flow<UserEntity>
    fun getLoungeUserList_Remote (status: Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser_Remote(userEntity: UserEntity)

    suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>
    //fun signIn(credential: PhoneAuthCredential) : Flow<Results<Int>>
    //suspend fun updateUriList (uriList: MutableList<String>, status: Int)
    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)


    suspend fun checkNickName (nickName : String) :Results<Int>
    suspend fun getUsersWithGeoHash (latitude : Double, longitude : Double, radiusInMeter : Int) : List<UserEntity>

    //suspend fun uploadImage (uriMap: MutableMap<Int, Uri>)
    suspend fun uploadImage (uriMap: MutableMap<String, Uri>)
    fun deleteImages(indexes : MutableList<Int>)


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    fun likeUser (otherUserEntity: UserEntity, ownUser : UserEntity)
    fun skipUser(otherUserEntity: UserEntity, ownUser: UserEntity)

    suspend fun checkLoungeCount(status: Int, localCount : Int) : Int
    suspend fun getNewLoungeUser(status: Int, newCount : Int) : MutableList<UserEntity>
    fun updateLoungeUser (status: Int) : Flow<MutableList<UserEntity>>

    fun likeBack(otherUser : UserEntity, ownUser: UserEntity)

}