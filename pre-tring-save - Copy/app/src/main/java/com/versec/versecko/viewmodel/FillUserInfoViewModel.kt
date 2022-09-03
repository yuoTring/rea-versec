package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Results
import kotlinx.coroutines.launch

class FillUserInfoViewModel (

    private val repository: UserRepository

        ) : ViewModel() {



                fun checkNickName (nickName : String) : LiveData<Results<Int>> {

                    val result = MutableLiveData<Results<Int>>()

                    viewModelScope.launch {
                        result.postValue(repository.checkNickName(nickName))
                    }

                    return result
                }
}