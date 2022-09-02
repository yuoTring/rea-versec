package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository

class ProfileViewModel (

        private val repository: UserRepository

        ) : ViewModel() {


                //private
                val _user : LiveData<UserEntity> = repository.getOwnUser_Local().asLiveData()
        var userEntity = UserEntity()








}