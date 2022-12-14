package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.util.Response

class RoomViewModel (

    private val repository: ChatRepository

        ) : ViewModel() {


            fun getMessages (roomUid : String) = repository.getMessages(roomUid).asLiveData()


            private suspend fun _sendMessage (content : String, room : RoomEntity) : Response<Int> {
                return repository.sendMessage(content, room)
            }

            suspend fun sendMessage (content: String, room: RoomEntity) : Response<Int> {
                return _sendMessage(content, room)
            }

            private suspend fun _resetUnreadMessageCounter (chatRoomUid : String) : Response<Int>
                = repository.resetUnreadMessageCounter(chatRoomUid)

            suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
                = _resetUnreadMessageCounter(chatRoomUid)

            private suspend fun _resetReadOrNot (message : MessageEntity) : Response<Int>
                = repository.resetReadOrNot(message)

            suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>
                = _resetReadOrNot(message)







}