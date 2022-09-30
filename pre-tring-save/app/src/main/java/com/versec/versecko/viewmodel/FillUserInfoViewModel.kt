package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.launch

class FillUserInfoViewModel (

    private val userRepository: UserRepository


        ) : ViewModel() {



                fun checkNickName (nickName : String) : LiveData<Response<Int>> {

                    val result = MutableLiveData<Response<Int>>()

                    viewModelScope.launch {
                        result.postValue(userRepository.checkNickName(nickName))
                    }

                    return result
                }
}