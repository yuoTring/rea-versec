package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository

class DetailProfileViewModel (

    private val userRepository: UserRepository

        ) : ViewModel() {


            var userEntity = UserEntity()

}