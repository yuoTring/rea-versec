package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.launch

class SignInViewModel (

    private val userRepository: UserRepository


        ) : ViewModel() {





    /**
            suspend fun signIn (credential: PhoneAuthCredential) : Results<Int> {

                viewModelScope.launch {
                    val signInResult = repository.signIn(credential)


                }

            }**/


    fun signIn (credential: PhoneAuthCredential) : LiveData<Response<Int>> {

        val signInResult = MutableLiveData<Response<Int>>()

        viewModelScope.launch {

            signInResult.postValue(userRepository.signIn(credential))
        }

        return signInResult
    }


}