package com.versec.versecko.data.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.datasource.local.UserLocalDataSource
import com.versec.versecko.data.datasource.remote.UserRemoteDataSource
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl (
    //private val localDataSource : UserLocalDataSource,
    //private val remoteDataSource: UserRemoteDataSource

    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource

) :UserRepository
{
    override fun getOwnUser_Local(): Flow<UserEntity> {
        return localDataSource.getOwnUser()
    }

    override suspend fun insertUser_Local(userEntity: UserEntity) {

        localDataSource.insertUser(userEntity)
    }

    override suspend fun updateUriList(uriMap: MutableMap<String, String>, status: Int) {

        localDataSource.updateUriList(uriMap, status)
    }

    override fun saveDuplicateUser(uid: String) {
        localDataSource.saveDuplicateUser(uid)
    }

    override fun getDuplicateUsers(): MutableList<String>? {
        return localDataSource.getDuplicateUsers()
    }

    override fun setGenderValue(genderValue: String) {
        localDataSource.setGenderValue(genderValue)
    }

    override fun setAgeRange(min: Int, max: Int) {
        localDataSource.setAgeRange(min, max)
    }

    override fun setDistance(distance: Int) {
        localDataSource.setDistance(distance)
    }

    override fun getGenderValue(): String? {
        return localDataSource.getGenderValue()
    }

    override fun getAgeRange(): List<Int>? {
        return localDataSource.getAgeRange()
    }

    override fun getDistance(): Int? {
        return localDataSource.getDistance()
    }

    override fun setCounter(status: Int, count: Int) {

        if (status == 2) {
            localDataSource.setLikedCounter(count)
        }else {
            localDataSource.setMatchingCounter(count)
        }
    }

    override fun getCounter(status: Int): Int? {
        if (status == 2) {
            return localDataSource.getLikedCounter()
        }else {
            return localDataSource.getMatchingCounter()
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun getOwnUser_Remote(): Flow<UserEntity> {

        return remoteDataSource.getOwnUser()
    }

    override suspend fun insertUser_Remote(userEntity: UserEntity) {

        remoteDataSource.insertUser(userEntity)

    }

    override suspend fun signIn(credential: PhoneAuthCredential): Response<Int> {

        return remoteDataSource.signIn(credential)
    }

    override suspend fun checkNickName(nickName: String): Response<Int> {
        return remoteDataSource.checkNickName(nickName)
    }

    override suspend fun getUsersWithGeoHash(
        latitude: Double,
        longitude: Double,
        radiusInMeter: Int,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {
        return remoteDataSource.getUsersWithGeoHash(latitude, longitude, radiusInMeter, gender, minAge, maxAge)
    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>) {

        remoteDataSource.uploadImage(uriMap)
    }

    override fun deleteImage(index: Int) {
        remoteDataSource.deleteImage(index)
    }

    override suspend fun reuploadImage(index: Int, uri: Uri) {
        remoteDataSource.reuploadImage(index, uri)
    }

    override suspend fun likeUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {

        localDataSource.saveDuplicateUser(otherUser.uid)

        return remoteDataSource.likeUser(otherUser, ownUser)
    }


    override fun skipUser(otherUser: UserEntity) {
        remoteDataSource.skipUser(otherUser)
    }

    override fun getLoungeUsers(status: Int): Flow<Response<MutableList<UserEntity>>> {

        return remoteDataSource.getLoungeUsers(status)
    }

    override suspend fun matchUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {
        return remoteDataSource.matchUser(otherUser, ownUser)
    }

    override suspend fun deleteMatch(otherUid: String): Response<Int> {
        return remoteDataSource.deleteMatch(otherUid)
    }

    override suspend fun rejectLiked(otherUser: UserEntity): Response<Int> {
        return remoteDataSource.rejectLiked(otherUser)
    }

    override suspend fun rejectMatched(otherUser: UserEntity): Response<Int> {
        return remoteDataSource.rejectMatched(otherUser)
    }


}