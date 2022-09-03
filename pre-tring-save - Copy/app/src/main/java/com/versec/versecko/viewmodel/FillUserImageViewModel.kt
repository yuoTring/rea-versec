package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class FillUserImageViewModel (

    private val userRepository: UserRepository

        ) : ViewModel(){


            fun uploadImage (uriMap : MutableMap<String, Uri>)
            {

                viewModelScope.launch {

                    userRepository.uploadImage(uriMap)
                }
            }
}