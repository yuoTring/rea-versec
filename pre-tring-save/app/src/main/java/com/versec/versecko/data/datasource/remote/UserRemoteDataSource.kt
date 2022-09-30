package com.versec.versecko.data.datasource.remote

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.DocumentSnapshot
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    fun getOwnUser () : Flow<UserEntity>
    suspend fun insertUser (userEntity: UserEntity)

    //fun signIn (credential: PhoneAuthCredential) : Flow<Results<Int>>
    suspend fun signIn(credential: PhoneAuthCredential) : Response<Int>
//suspend fun signIn(credential: PhoneAuthCredential) : Results<Int>


    suspend fun checkNickName (nickName : String) :Response<Int>

    suspend fun getUsersWithGeoHash (latitude: Double, longitude: Double, radiusInMeter: Int, gender : String, minAge : Int, maxAge : Int) : Response<MutableList<UserEntity>>
    suspend fun getUsersWithCondition (conditions : List<String>) : Response<MutableList<UserEntity>>

    suspend fun likeUser (otherUser: UserEntity, ownUser: UserEntity) : Response<Int>
    fun skipUser (otherUser: UserEntity)

    suspend fun uploadImage (uriMap: MutableMap<String, Uri>)
    fun deleteImage (index : Int)
    suspend fun reuploadImage (index: Int, uri : Uri)

    fun getLoungeUsers (status: Int) : Flow<Response<MutableList<UserEntity>>>

    suspend fun matchUser (otherUser : UserEntity, ownUser: UserEntity) : Response<Int>
    suspend fun deleteMatch (otherUid : String) : Response<Int>

    suspend fun rejectLiked (otherUser: UserEntity) : Response<Int>
    suspend fun rejectMatched (otherUser: UserEntity) : Response<Int>







}