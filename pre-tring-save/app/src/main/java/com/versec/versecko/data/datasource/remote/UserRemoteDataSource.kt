package com.versec.versecko.data.datasource.remote

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.DocumentSnapshot
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRemoteDataSource {

    fun getOwnUser () : Flow<UserEntity>
    fun getLoungeUserList (status : Int) : Flow<MutableList<UserEntity>>
    suspend fun insertUser (userEntity: UserEntity)

    //fun signIn (credential: PhoneAuthCredential) : Flow<Results<Int>>
    suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>
//suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>


    suspend fun checkNickName (nickName : String) :Results<Int>

    suspend fun getUsersWithGeoHash (latitude : Double, longitude : Double, radiusInMeter : Int) : List<UserEntity>
    fun getUsers(latitude: Double, longitude: Double, radiusInMeter: Int) : Flow<MutableList<DocumentSnapshot>>

    fun likeUser (otherUserEntity: UserEntity, ownUser : UserEntity)
    fun skipUser (otherUserEntity: UserEntity, ownUser: UserEntity)

    suspend fun uploadImage (uriMap: MutableMap<String, Uri>)
    fun deleteImages (indexes : MutableList<Int>)

    suspend fun checkLoungeCount(status: Int, localCount : Int) : Int
    suspend fun getNewLoungeUser(status: Int, newCount : Int) : MutableList<UserEntity>
    fun updateLoungeUser (status: Int) : Flow<MutableList<UserEntity>>

    fun likeBack(otherUser : UserEntity, ownUser: UserEntity)


}