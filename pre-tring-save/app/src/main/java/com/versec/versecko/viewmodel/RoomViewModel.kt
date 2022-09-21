package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.util.SingleLiveData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoomViewModel (

    private val repository: ChatRepository

        ) : ViewModel() {



            fun getAllMessage (roomUid : String) : LiveData<MutableList<MessageEntity>> {


                val _messageList = MutableLiveData<MutableList<MessageEntity>>()

                viewModelScope.launch {

                    _messageList.postValue(repository.getAllMessages(roomUid))
                }


                return _messageList
            }


            fun sendMessage(contents : String, room : ChatRoomEntity ) {

                repository.sendMessage(contents, room)

            }


        fun set (roomUid: String) : LiveData<MessageEntity> {


            val temp : LiveData<MessageEntity> = repository.getUpdatedMessage(roomUid).asLiveData()

            val up : SingleLiveData<MessageEntity> = temp as SingleLiveData<MessageEntity>

            return up
        }










}