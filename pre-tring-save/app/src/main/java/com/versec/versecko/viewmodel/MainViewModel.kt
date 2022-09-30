package com.versec.versecko.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(

    userRepository: UserRepository

) : ViewModel() {


    private val _userLocal : MutableLiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
    val userLocal : LiveData<UserEntity> get() = _userLocal


    /**
    private val _genderFilter = MutableLiveData<String> ()

    fun setGender (gender : String) {
        _setGender(gender)
    }

    private fun _setGender (gender: String) {

        viewModelScope.launch {

            delay(500)
            _genderFilter.value = gender
        }
    } **/
}