package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class SignInViewModel (

    private val userRepository: UserRepository


        ) : ViewModel() {


    private suspend fun _signIn (credential: PhoneAuthCredential) : Response<Int> {
        return userRepository.signIn(credential)
    }

    suspend fun signIn (credential: PhoneAuthCredential) : Response<Int> {
        return _signIn(credential)
    }

    private suspend fun _getOwnUser () : Response<UserEntity?> {
        return userRepository.getOwnUserOneShot()
    }

    suspend fun getOwnUser () : Response<UserEntity?> {
        return _getOwnUser()
    }

    private fun _setGenderValue (gender : String) {
        userRepository.setGenderValue(gender)
    }

    fun setGender (gender: String) {
        _setGenderValue(gender)
    }

    private fun _setAgeRange (min : Int, max : Int) {
        userRepository.setAgeRange(min, max)
    }

    fun setAgeRange (min: Int, max: Int) {
        _setAgeRange(min, max)
    }

    private fun _setDistance (distance : Int) {
        userRepository.setDistance(distance)
    }

    fun setDistance (distance: Int) {
        _setDistance(distance)
    }

    private suspend fun _insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return userRepository.insertUser_Local(userEntity)
    }

    suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return _insertUser_Local(userEntity)
    }






}