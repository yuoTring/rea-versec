package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository

class MainViewModel(

    userRepository: UserRepository

) : ViewModel() {


    private val _userLocal : MutableLiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
    val userLocal : LiveData<UserEntity> get() = _userLocal


}