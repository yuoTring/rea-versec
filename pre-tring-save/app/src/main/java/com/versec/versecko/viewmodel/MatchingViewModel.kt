package com.versec.versecko.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class MatchingViewModel (

    private val userRepository: UserRepository


        ) : ViewModel() {



    val _ownUser : LiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData()


            fun getUsersWithGeoHash (latitude: Double,
                                     longitude: Double,
                                     radiusInMeter: Int) : LiveData<List<UserEntity>> {

                val _userList = MutableLiveData<List<UserEntity>> ()



                viewModelScope.launch {


                    _userList.postValue(userRepository.getUsersWithGeoHash(latitude, longitude, radiusInMeter))
                }

                return _userList
            }

            fun likeUser (otherUserEntity: UserEntity, ownUserEntity: UserEntity) {

                userRepository.likeUser(otherUserEntity, ownUserEntity)

            }

            fun skipUser (otherUserEntity: UserEntity, ownUserEntity: UserEntity) {

                userRepository.skipUser(otherUserEntity, ownUserEntity)

            }


}