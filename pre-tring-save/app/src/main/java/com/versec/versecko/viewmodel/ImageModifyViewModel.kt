package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class ImageModifyViewModel (

    private val userRepository: UserRepository

        ) : ViewModel() {



            private val _ownUser : MutableLiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
            val ownUser = _ownUser



    private suspend fun _deleteImage (index: Int) : Response<Int> {
        return userRepository.deleteImage(index)
    }

    suspend fun deleteImage (index: Int) : Response<Int> {
        return _deleteImage(index)
    }

    private suspend fun _reuploadImage (index: Int, uri: Uri) : Response<Int> {
        return userRepository.reuploadImage(index, uri)
    }

    suspend fun reuploadImage (index: Int, uri: Uri) : Response<Int> {
        return _reuploadImage(index, uri)
    }

    private suspend fun _getOwnUser_Remote () : Response<UserEntity?> {
        return userRepository.getOwnUserOneShot()
    }

    suspend fun getOwnUser_Remote () : Response<UserEntity?> {
        return _getOwnUser_Remote()
    }

    private suspend fun _insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return userRepository.insertUser_Local(userEntity)
    }

    suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int> {
        return _insertUser_Local(userEntity)
    }



}