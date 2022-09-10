package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class FillUserImageViewModel (

    private val userRepository: UserRepository

        ) : ViewModel(){


            fun uploadImage (uriMap : MutableMap<String, Uri>)
            {

                viewModelScope.launch {

                    userRepository.uploadImage(uriMap)
                }
            }

            fun insertUser (userEntity: UserEntity) {

                viewModelScope.launch {
                    userRepository.insertUser_Remote(userEntity)
                    userRepository.insertUser_Local(userEntity)
                }
            }
}