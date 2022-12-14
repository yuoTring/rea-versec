package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class MatchingViewModel (

    private val repository: UserRepository


        ) : ViewModel() {



            suspend fun getUsersWithGeoHash (
                latitude: Double,
                longitude: Double,
                radiusInMeter: Int,
                gender : String,
                minAge : Int,
                maxAge : Int
            ): Response<MutableList<UserEntity>> {
                return _getUsersWithGeoHash(latitude, longitude, radiusInMeter, gender, minAge, maxAge)
            }

            private suspend fun _getUsersWithGeoHash (
                latitude: Double,
                longitude: Double,
                radiusInMeter: Int,
                gender : String,
                minAge : Int,
                maxAge : Int) : Response<MutableList<UserEntity>> {

                return repository.getUsersWithGeoHash(latitude, longitude, radiusInMeter, gender, minAge, maxAge)
            }

            private suspend fun _getUsersWithStyles (

                styles : List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int

            ) : Response<MutableList<UserEntity>> {
                return repository.getUsersWithStyle(styles, gender, minAge, maxAge)
            }

            suspend fun getUsersWithStyles (
                styles: List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int
            ) : Response<MutableList<UserEntity>> {

                return _getUsersWithStyles(styles, gender, minAge, maxAge)
            }

            private suspend fun _getUsersWithResidences (

                residences : List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int
            ) : Response<MutableList<UserEntity>> {

                return repository.getUsersWithResidence(residences, gender, minAge, maxAge)
            }

            suspend fun getUsersWithResidences (
                residences: List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int
            ) : Response<MutableList<UserEntity>> {

                return _getUsersWithResidences(residences, gender, minAge, maxAge)
            }

            suspend fun getUsersWithPlaces (
                places: List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int
            ) : Response<MutableList<UserEntity>> {
                return _getUsersWithPlaces(places, gender, minAge, maxAge)
            }

            private suspend fun _getUsersWithPlaces (

                places: List<String>,
                gender: String,
                minAge: Int,
                maxAge: Int
            ) : Response<MutableList<UserEntity>> {
                return repository.getUsersWithPlace(places, gender, minAge, maxAge)
            }








            suspend fun likeUser (otherUser : UserEntity, ownUser : UserEntity) : Response<Int> {
                return _likeUser(otherUser, ownUser)
            }

            private suspend fun _likeUser (otherUser: UserEntity, ownUser: UserEntity) : Response<Int> {
                return repository.likeUser(otherUser, ownUser)
            }


            fun skipUser (otherUser: UserEntity) {
                _skipUser(otherUser)
            }

            private fun _skipUser (otherUser: UserEntity) {
                repository.skipUser(otherUser)
            }






            private fun _getGenderValue () : String? {
                return repository.getGenderValue()
            }

            fun getGender () : String? {
                return _getGenderValue()
            }

            private fun _getAgeRange () : List<Int>? {
                return repository.getAgeRange()
            }

            fun getAgeRange () : List<Int>? {
                return _getAgeRange()
            }

            private fun _getDistance () : Int? {
                return repository.getDistance()
            }

            fun getDistance () : Int? {
                return _getDistance()
            }

            fun getOverlappingUsers () = repository.getDuplicateUsers()
            fun saveOverlappingUser (uid : String) = repository.saveDuplicateUser(uid)




}