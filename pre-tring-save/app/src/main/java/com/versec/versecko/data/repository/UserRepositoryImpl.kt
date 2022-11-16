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

    override suspend fun insertUser_Local(userEntity: UserEntity): Response<Int> {
        return localDataSource.insertUser(userEntity)
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

    override fun setTimestamp() {
        localDataSource.setTimestamp()
    }

    override fun getTimestamp(): Long {
        return localDataSource.getTimestamp()
    }


    override fun setMatchingNotification(on: Boolean) {

        localDataSource.setMatchingNotification(on)
    }

    override fun setLikedNotification(on: Boolean) {

        localDataSource.setLikedNotification(on)
    }

    override fun setChatNotification(on: Boolean) {

        localDataSource.setChatNotification(on)
    }

    override fun setKnockNotification(on: Boolean) {

        localDataSource.setKnockNotification(on)
    }

    override fun setMarketingNotification(on: Boolean) {

        localDataSource.setMarketingNotification(on)
    }

    override fun getMatchingNotification(): Boolean {

        return localDataSource.getMatchingNotification()
    }

    override fun getLikedNotification(): Boolean {

        return localDataSource.getLikedNotification()
    }

    override fun getChatNotification(): Boolean {

        return localDataSource.getChatNotification()
    }

    override fun getKnockNotification(): Boolean {

        return localDataSource.getKnockNotification()
    }

    override fun getMarketingNotification(): Boolean {

        return localDataSource.getMarketingNotification()
    }

    override suspend fun updateDeletedAt(activate: Boolean): Response<Int> {
        return localDataSource.updateDeletedAt(activate)
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun getOwnUser_Remote(): Flow<UserEntity> {

        return remoteDataSource.getOwnUser()
    }

    override suspend fun getOwnUserOneShot(): Response<UserEntity?> {
        return remoteDataSource.getOwnUserOneShot()
    }

    override suspend fun insertUser_Remote(userEntity: UserEntity): Response<Int> {
        return remoteDataSource.insertUser(userEntity)
    }


    override suspend fun signIn(credential: PhoneAuthCredential): Response<Int> {

        return remoteDataSource.signIn(credential)
    }

    override fun getUid(): Response<String?> {
        return remoteDataSource.getUid()
    }

    override suspend fun checkUid(uid: String): Response<Int> {
        return remoteDataSource.checkUid(uid)
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

    override suspend fun getUsersWithPlace(
        places: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        return remoteDataSource.getUsersWithPlace(places, gender, minAge, maxAge)
    }

    override suspend fun getUsersWithResidence(
        residences: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        return remoteDataSource.getUsersWithResidence(residences, gender, minAge, maxAge)
    }

    override suspend fun getUsersWithStyle(
        styles: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        return remoteDataSource.getUsersWithStyle(styles, gender, minAge, maxAge)
    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>): Response<Int> {
        return remoteDataSource.uploadImage(uriMap)
    }

    override suspend fun deleteImage(index: Int): Response<Int> {
        return remoteDataSource.deleteImage(index)
    }


    override suspend fun reuploadImage(index: Int, uri: Uri): Response<Int> {
        return remoteDataSource.reuploadImage(index, uri)
    }

    override suspend fun getFCMToken(): Response<String> {
        return remoteDataSource.getFCMToken()
    }

    override suspend fun likeUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {

        localDataSource.saveDuplicateUser(otherUser.uid)

        return remoteDataSource.likeUser(otherUser, ownUser)
    }


    override fun skipUser(otherUser: UserEntity) {
        remoteDataSource.skipUser(otherUser)
    }

    override fun getLoungeUsers(status: Int): Flow<Response<MutableMap<Long, UserEntity>>> {

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

    override fun logOut(): Response<Int> {
        return remoteDataSource.logOut()
    }

    override suspend fun deleteAccount(): Response<Int> {
        return remoteDataSource.deleteAccount()
    }

    override suspend fun inactivateAccount(): Response<Int> {
        return remoteDataSource.inactivateAccount()
    }

    override suspend fun activateAccount(): Response<Int> {
        return remoteDataSource.activateAccount()
    }


}