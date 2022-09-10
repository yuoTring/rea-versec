package com.versec.versecko.viewmodel

import android.util.Log
import androidx.lifecycle.*
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



        val _allUser : LiveData<MutableList<UserEntity>> = liveData {

                val data = userRepository.getAllUser()

                Log.d("lounge-users", data.toString())
        }

        fun allUser() {
                viewModelScope.launch {

                        val users =
                        userRepository.getAllUser()

                        Log.d("lounge-get", users.toString())


                }
        }








}