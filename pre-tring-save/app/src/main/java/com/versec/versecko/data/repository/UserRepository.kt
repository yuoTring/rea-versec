package com.versec.versecko.data.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {


    fun getOwnUser_Local () : Flow<UserEntity>
    suspend fun insertUser_Local (userEntity: UserEntity)

    fun saveDuplicateUser (uid : String)
    fun getDuplicateUsers () : MutableList<String>?

    fun setGenderValue (genderValue : String)
    fun setAgeRange (min : Int, max : Int)
    fun setDistance (distance : Int)

    fun getGenderValue () : String?
    fun getAgeRange() : List<Int>?
    fun getDistance() : Int?

    fun setCounter (status: Int, count : Int)
    fun getCounter (status: Int) : Int?


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun getOwnUser_Remote () : Flow<UserEntity>
    suspend fun insertUser_Remote(userEntity: UserEntity)

    suspend fun signIn(credential: PhoneAuthCredential) : Response<Int>
    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)


    suspend fun checkNickName (nickName : String) :Response<Int>
    suspend fun getUsersWithGeoHash (latitude: Double, longitude: Double, radiusInMeter: Int, gender : String, minAge : Int, maxAge : Int) : Response<MutableList<UserEntity>>

    suspend fun uploadImage (uriMap: MutableMap<String, Uri>)
    fun deleteImage (index : Int)
    suspend fun reuploadImage (index: Int, uri: Uri)


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    suspend fun likeUser (otherUser: UserEntity, ownUser: UserEntity) : Response<Int>
    fun skipUser (otherUser : UserEntity)

    fun getLoungeUsers (status: Int) : Flow<Response<MutableList<UserEntity>>>

    suspend fun matchUser (otherUser : UserEntity, ownUser: UserEntity) : Response<Int>
    suspend fun deleteMatch (otherUid : String) : Response<Int>

    suspend fun rejectLiked (otherUser: UserEntity) : Response<Int>
    suspend fun rejectMatched (otherUser: UserEntity) : Response<Int>


}