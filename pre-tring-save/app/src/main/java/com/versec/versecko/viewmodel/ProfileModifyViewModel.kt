package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileModifyViewModel (

    private val userRepository: UserRepository


        ) : ViewModel() {

            val _user : LiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData()




    fun updateUser (userEntity: UserEntity, uriMap : MutableMap<String, Uri>, deleteIndexes: MutableList<Int>) {
        update(userEntity, uriMap, deleteIndexes)
    }

    private fun update (userEntity: UserEntity, uriMap: MutableMap<String, Uri>, deleteIndexes : MutableList<Int>) {

        viewModelScope.launch {

            if (deleteIndexes.size>0)
                userRepository.deleteImages(deleteIndexes)

            userRepository.insertUser_Local(userEntity)
            userRepository.insertUser_Remote(userEntity)
            userRepository.uploadImage(uriMap)

        }

    }


}