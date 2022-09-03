package com.versec.versecko.data.datasource.remote

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
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



    suspend fun insertImage(mutableList: MutableList<Bitmap>) : Results<Int>
    fun getImageUri () : Flow<String>

    suspend fun checkNickName (nickName : String) :Results<Int>

    suspend fun getUsersWithGeoHash (latitude : Double, longitude : Double, radiusInMeter : Int) : List<UserEntity>

    suspend fun likeUser (userEntity: UserEntity)

    suspend fun uploadImage (uriMap: MutableMap<String, Uri>)

}