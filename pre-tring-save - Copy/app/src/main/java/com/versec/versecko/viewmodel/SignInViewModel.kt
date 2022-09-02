package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Results
import kotlinx.coroutines.launch

class SignInViewModel (

        private val repository: UserRepository

        ) : ViewModel() {





    /**
            suspend fun signIn (credential: PhoneAuthCredential) : Results<Int> {

                viewModelScope.launch {
                    val signInResult = repository.signIn(credential)


                }

            }**/


    fun signIn (credential: PhoneAuthCredential) : LiveData<Results<Int>> {

        val signInResult = MutableLiveData<Results<Int>>()

        viewModelScope.launch {

            signInResult.postValue(repository.signIn(credential))
        }

        return signInResult
    }


}