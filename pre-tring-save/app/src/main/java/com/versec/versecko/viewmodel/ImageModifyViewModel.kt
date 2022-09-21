package com.versec.versecko.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository

class ImageModifyViewModel (

    private val repository: UserRepository

        ) : ViewModel() {



            private val _ownUser : MutableLiveData<UserEntity> = repository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
            val ownUser = _ownUser



    fun deleteImage () {

    }

    private fun delete () {

    }


}