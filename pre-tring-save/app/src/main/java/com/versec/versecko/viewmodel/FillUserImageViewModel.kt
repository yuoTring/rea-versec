package com.versec.versecko.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.launch

class FillUserImageViewModel (

    private val userRepository: UserRepository

        ) : ViewModel(){



            private suspend fun _uploadImage (uriMap: MutableMap<String,Uri>) : Response<Int> {
                return userRepository.uploadImage(uriMap)
            }

            suspend fun uploadImage (uriMap: MutableMap<String, Uri>) : Response<Int> {
                return _uploadImage(uriMap)
            }

            private suspend fun _insertUser_Remote (userEntity: UserEntity) : Response<Int> {
                return userRepository.insertUser_Remote(userEntity)
            }

            suspend fun insertUser_Remote (userEntity: UserEntity) : Response<Int> {
                return _insertUser_Remote(userEntity)
            }


            private suspend fun _insertUser_Local (userEntity: UserEntity) : Response<Int> {
                return userRepository.insertUser_Local(userEntity)
            }

            suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int> {
                return _insertUser_Local(userEntity)
            }

            private suspend fun _getOwnUser () : Response<UserEntity?> {
                return userRepository.getOwnUserOneShot()
            }

            suspend fun getOwnUser () : Response<UserEntity?> {
                return _getOwnUser()
            }






            private suspend fun _saveFCMToken () : Response<Int> {
                return userRepository.saveFCMToken()
            }

            suspend fun saveFCMToken () : Response<Int> {
                return _saveFCMToken()
            }



}