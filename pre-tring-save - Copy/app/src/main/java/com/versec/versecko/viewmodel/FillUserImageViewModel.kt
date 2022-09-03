package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class FillUserImageViewModel (

    private val userRepository: UserRepository

        ) : ViewModel(){


            fun uploadImage (file :File) {

                viewModelScope.launch {

                    userRepository.uploadImage(file)
                }
            }
}