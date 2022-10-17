package com.versec.versecko.data.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {


    fun getOwnUser_Local () : Flow<UserEntity>
    suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int>

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


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun getOwnUser_Remote () : Flow<UserEntity>
    suspend fun getOwnUserOneShot () : Response<UserEntity?>

    suspend fun insertUser_Remote (userEntity: UserEntity) : Response<Int>

    suspend fun signIn(credential: PhoneAuthCredential) : Response<Int>

    fun getUid () : Response<String?>
    suspend fun checkUid(uid : String) : Response<Int>

    suspend fun updateUriList (uriMap: MutableMap<String,String>, status: Int)


    suspend fun checkNickName (nickName : String) :Response<Int>
    suspend fun getUsersWithGeoHash (latitude: Double, longitude: Double, radiusInMeter: Int, gender : String, minAge : Int, maxAge : Int) : Response<MutableList<UserEntity>>
    suspend fun getUsersWithPlace (places : List<String>, gender: String, minAge: Int, maxAge: Int) : Response<MutableList<UserEntity>>
    suspend fun getUsersWithResidence (residences : List<String>, gender: String, minAge: Int, maxAge: Int) : Response<MutableList<UserEntity>>
    suspend fun getUsersWithStyle (styles : List<String>, gender: String, minAge: Int, maxAge: Int) : Response<MutableList<UserEntity>>

    suspend fun uploadImage (uriMap: MutableMap<String, Uri>) : Response<Int>
    suspend fun deleteImage (index: Int) : Response<Int>
    suspend fun reuploadImage (index: Int, uri: Uri) : Response<Int>

    suspend fun saveFCMToken () : Response<Int>
    suspend fun getFCMToken () : Response<String>
    suspend fun postFCMToken (token : String) : Response<Int>

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    suspend fun likeUser (otherUser: UserEntity, ownUser: UserEntity) : Response<Int>
    fun skipUser (otherUser : UserEntity)

    fun getLoungeUsers (status: Int) : Flow<Response<MutableList<UserEntity>>>

    suspend fun matchUser (otherUser : UserEntity, ownUser: UserEntity) : Response<Int>
    suspend fun deleteMatch (otherUid : String) : Response<Int>

    suspend fun rejectLiked (otherUser: UserEntity) : Response<Int>
    suspend fun rejectMatched (otherUser: UserEntity) : Response<Int>

    fun logOut() : Response<Int>
    suspend fun deleteAccount () : Response<Int>
    suspend fun inactivateAccount () : Response<Int>
    suspend fun activateAccount() : Response<Int>

}