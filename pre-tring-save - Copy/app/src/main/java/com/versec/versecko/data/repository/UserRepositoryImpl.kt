package com.versec.versecko.data.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.datasource.local.UserLocalDataSource
import com.versec.versecko.data.datasource.remote.UserRemoteDataSource
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow
import java.io.File

class UserRepositoryImpl (
    private val localDataSource : UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource

) :UserRepository
{

    /**
    override suspend fun getAllUser() : MutableList<UserModel>
    {
    val userList : List<UserEntity> = localDataSource.getAllUser()
    var temp = mutableListOf<UserModel>()


    for (user in userList)
    temp.add(userMapper.entityToModel(user))

    return temp
    }


    override suspend fun insertUser(userModel: UserModel) {

    return localDataSource.insertUser(userMapper.modelToEntity(userModel))
    }**/
    override fun getOwnUser_Local(): Flow<UserEntity> {
        return localDataSource.getOwnUser()
    }

    override fun getUserList_Local(status: Int): Flow<MutableList<UserEntity>> {
        return localDataSource.getUserList(status)
    }

    override suspend fun insertUser_Local(userEntity: UserEntity) {

        localDataSource.insertUser(userEntity)
    }

    override suspend fun updateUriList(uriMap: MutableMap<Int, String>, status: Int) {

        localDataSource.updateUriList(uriMap, status)
    }




    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun getOwnUser_Remote(): Flow<UserEntity> {

        return remoteDataSource.getOwnUser()
    }

    override fun getLoungeUserList_Remote(status: Int): Flow<MutableList<UserEntity>> {

        return remoteDataSource.getLoungeUserList(status)

    }

    override suspend fun insertUser_Remote(userEntity: UserEntity) {

        remoteDataSource.insertUser(userEntity)

    }

    override suspend fun signIn(credential: PhoneAuthCredential): Results<Int> {

        return remoteDataSource.signIn(credential)
    }

    override suspend fun checkNickName(nickName: String): Results<Int> {
        return remoteDataSource.checkNickName(nickName)
    }

    override suspend fun getUsersWithGeoHash(
        latitude: Double,
        longitude: Double,
        radiusInMeter: Int
    ): List<UserEntity> {

        return remoteDataSource.getUsersWithGeoHash(latitude, longitude, radiusInMeter)
    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>) {

        remoteDataSource.uploadImage(uriMap)
    }


    override suspend fun likeUser(userEntity: UserEntity) {

        localDataSource.insertUser(userEntity)

        remoteDataSource.likeUser(userEntity)
    }


}