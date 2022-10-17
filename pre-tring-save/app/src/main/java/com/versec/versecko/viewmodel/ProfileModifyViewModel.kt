package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.launch

class ProfileModifyViewModel (

    private val userRepository: UserRepository

        ) : ViewModel() {



            private val _user : MutableLiveData<UserEntity> =userRepository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
            val user : LiveData<UserEntity> = _user


    private suspend fun _insertUser_Remote (userEntity: UserEntity) : Response<Int> {
        return userRepository.insertUser_Remote(userEntity)
    }

    suspend fun insertUser_Remote (userEntity: UserEntity) : Response<Int> {
        return _insertUser_Remote(userEntity)
    }


    private suspend fun _insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return userRepository.insertUser_Local(userEntity)
    }

    suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return _insertUser_Local(userEntity)
    }






}