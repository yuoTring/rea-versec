package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileModifyViewModel (

    private val repository: UserRepository

        ) : ViewModel() {



            val _user : LiveData<UserEntity> = repository.getOwnUser_Local().asLiveData()


            fun updateOwnUser (userEntity: UserEntity) {

                viewModelScope.launch {

                    repository.insertUser_Local(userEntity)
                    repository.insertUser_Remote(userEntity)
                }
            }


}