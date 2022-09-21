package com.versec.versecko.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel (

    private val userRepository: UserRepository,
    private val repository: ChatRepository

        ) : ViewModel(){


    private val _usersLiked : MutableLiveData<MutableList<UserEntity>> = userRepository.getUserList_Local(2).asLiveData() as MutableLiveData<MutableList<UserEntity>>
    val usersLiked : LiveData<MutableList<UserEntity>> get() = _usersLiked

    private val _usersMatched : MutableLiveData<MutableList<UserEntity>> = userRepository.getUserList_Local(3).asLiveData() as MutableLiveData<MutableList<UserEntity>>
    val usersMatched : LiveData<MutableList<UserEntity>> = _usersMatched

    private val _usersLikedUpdated : MutableLiveData<MutableList<UserEntity>> = userRepository.updateLoungeUser(2).asLiveData() as MutableLiveData<MutableList<UserEntity>>
    val usersLikedUpdated : LiveData<MutableList<UserEntity>> get() = _usersLikedUpdated

    private val _usersMatchedUpdated : MutableLiveData<MutableList<UserEntity>> = userRepository.updateLoungeUser(3).asLiveData() as MutableLiveData<MutableList<UserEntity>>
    val usersMatchedUpdated : LiveData<MutableList<UserEntity>> get() = _usersMatchedUpdated




            fun getLoungeUser (status : Int) {

                var localCount = -2
                var newCount = -2

                CoroutineScope(Dispatchers.IO).launch {

                    val jobCheckLocalCount = viewModelScope.launch {


                        localCount = userRepository.checkLocalLoungeCount(status)

                        Log.d("local-count-lounge", "local Count: "+localCount)
                    }

                    jobCheckLocalCount.join()

                    val jobCheckRemoteCount = viewModelScope.launch {

                        newCount = userRepository.checkLoungeCount(status, localCount)
                        Log.d("local-count-lounge", "new Count: "+newCount)

                    }

                    jobCheckRemoteCount.join()

                    if (newCount != 0 ) {

                        viewModelScope.launch {

                            var newUsers = mutableListOf<UserEntity>()


                            newUsers.addAll(userRepository.getNewLoungeUser(status, newCount))

                            newUsers.forEach {

                                if (status == 2) it.loungeStatus = 2
                                else it.loungeStatus = 3

                                Log.d("local-count-result", it.toString())
                                userRepository.insertUser_Local(it)

                            }





                        }
                    }


                }





            }


    private val _roomUpdated : MutableLiveData<ChatRoomEntity> = repository.getUpdatedChatRoom().asLiveData() as MutableLiveData<ChatRoomEntity>
    val roomUpdated : LiveData<ChatRoomEntity> get() = _roomUpdated


    fun openRoom (otherUser : UserEntity, ownUser : UserEntity) {
        open(otherUser, ownUser)
    }

    private fun open (otherUser: UserEntity, ownUser: UserEntity) {

        repository.openChatRoom(otherUser, ownUser)

    }








}