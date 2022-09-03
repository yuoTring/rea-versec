package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.launch

class MatchingViewModel (

    private val repository: UserRepository

        ) : ViewModel() {



            fun getUsersWithGeoHash (latitude: Double,
                                     longitude: Double,
                                     radiusInMeter: Int) : LiveData<List<UserEntity>> {

                val _userList = MutableLiveData<List<UserEntity>> ()

                viewModelScope.launch {


                    _userList.postValue(repository.getUsersWithGeoHash(latitude, longitude, radiusInMeter))
                }

                return _userList
            }


            fun likeUser (userEntity: UserEntity) {

                viewModelScope.launch {

                    userEntity.loungeStatus = 1
                    repository.likeUser(userEntity)

                }
            }


}