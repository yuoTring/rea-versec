package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel (

    private val userRepository: UserRepository

        ) : ViewModel(){


    var _loungeLikedUser : LiveData<MutableList<UserEntity>> = userRepository.getUserList_Local(2).asLiveData()
    var _loungeMatchedUser : LiveData<MutableList<UserEntity>> = userRepository.getUserList_Local(3).asLiveData()




            fun getLoungeUser (status : Int) {

                var localCount = -2
                var newCount = -2

                CoroutineScope(Dispatchers.IO).launch {

                    val jobCheckLocalCount = viewModelScope.launch {


                        localCount = userRepository.checkLocalLoungeCount(status)
                    }

                    jobCheckLocalCount.join()

                    val jobCheckRemoteCount = viewModelScope.launch {

                        newCount = userRepository.checkLoungeCount(status, localCount)
                    }

                    jobCheckRemoteCount.join()

                    if (newCount != 0 ) {

                        viewModelScope.launch {

                            var newUsers = mutableListOf<UserEntity>()


                            newUsers.addAll(userRepository.getNewLoungeUser(status, newCount))

                            newUsers.forEach {

                                if (status == 2) it.loungeStatus = 2
                                else it.loungeStatus = 3

                                userRepository.insertUser_Local(it)

                            }





                        }
                    }


                }





            }

    fun getLocalLoungeUser (status: Int) {

        viewModelScope.launch {




        }
    }


}