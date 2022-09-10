package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel (

        private val userRepository: UserRepository

        ) : ViewModel() {

        val _userLocal : LiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData()
        val _userRemote : LiveData<UserEntity> = userRepository.getOwnUser_Remote().asLiveData()

        var userEntity = UserEntity()

        fun insertUser_Local (userEntity: UserEntity) {

                viewModelScope.launch {

                        userRepository.insertUser_Local(userEntity)
                }
        }








}