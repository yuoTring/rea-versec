package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class ImageModifyViewModel (

    private val repository: UserRepository

        ) : ViewModel() {



            private val _ownUser : MutableLiveData<UserEntity> = repository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
            val ownUser = _ownUser



    fun deleteImage (index : Int) {
        delete(index)
    }

    private fun delete (index : Int) {
        repository.deleteImage(index)
    }

    fun uploadImage (index: Int, uri : Uri) {
        reuploadImage(index, uri)
    }

    private fun reuploadImage (index: Int, uri: Uri) {

        viewModelScope.launch {
            repository.reuploadImage(index, uri)
        }
    }


}