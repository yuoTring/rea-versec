package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel (

        private val userRepository: UserRepository

        ) : ViewModel() {

        private val _userRemote : MutableLiveData<UserEntity> = userRepository.getOwnUser_Remote().asLiveData() as MutableLiveData<UserEntity>
        val userRemote : LiveData<UserEntity> get() = _userRemote

        var userEntity = UserEntity()



        fun insertUser_Local (userEntity: UserEntity) {

                viewModelScope.launch {

                        userRepository.insertUser_Local(userEntity)
                }
        }

        fun insert (userEntity: UserEntity) {
                viewModelScope.launch {

                        userRepository.insertUser_Remote(userEntity)

                }
        }












}