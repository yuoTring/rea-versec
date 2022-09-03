package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository

class ProfileViewModel (

        private val repository: UserRepository

        ) : ViewModel() {


        val _userLcoal : LiveData<UserEntity> = repository.getOwnUser_Local().asLiveData()
        val _userRemote : LiveData<UserEntity> = repository.getOwnUser_Remote().asLiveData()

        var userEntity = UserEntity()








}