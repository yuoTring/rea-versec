package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.PhoneAuthCredential
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Results

class SignInViewModel (

        private val repository: UserRepository

        ) : ViewModel() {


    lateinit var _signInRequest : LiveData<Results<Int>>


    fun signIn (credential: PhoneAuthCredential) {

                _signInRequest= repository.signIn(credential).asLiveData()

            }
}