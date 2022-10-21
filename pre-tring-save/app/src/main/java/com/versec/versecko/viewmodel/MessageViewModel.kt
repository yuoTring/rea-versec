package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.util.Response

class MessageViewModel (

    private val repository: ChatRepository

        ) : ViewModel() {




            private suspend fun _resetUnreadMessageCounter (chatRoomUid : String) : Response<Int>
                = repository.resetUnreadMessageCounter(chatRoomUid)

            suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
                = _resetUnreadMessageCounter(chatRoomUid)

            private suspend fun _resetReadOrNot (message : MessageEntity) : Response<Int>
                = repository.resetReadOrNot(message)

            suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>
                = _resetReadOrNot(message)







}