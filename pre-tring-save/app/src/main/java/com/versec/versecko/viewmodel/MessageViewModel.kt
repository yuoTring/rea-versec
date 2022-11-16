package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.repository.RoomRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class MessageViewModel (

    private val repository: RoomRepository

        ) : ViewModel() {





            private fun _fetchMessage (roomUid : String) : Flow<Response<Map<Int, MessageEntity>>> {

                val flow = repository.fetchMessage(roomUid).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

                return flow
            }

            fun fetchMessage (roomUid: String) : Flow<Response<Map<Int, MessageEntity>>> {
                return _fetchMessage(roomUid)
            }





            private suspend fun _readMessage (roomUid: String, messageUid : String) : Response<Int> {
                return repository.readMessage(roomUid, messageUid)
            }

            suspend fun readMessage (roomUid: String, messageUid: String) : Response<Int> {
                return _readMessage(roomUid, messageUid)
            }

            private suspend fun _sendMessage (contents : String, roomUid: String) : Response<Int> {
                return repository.sendMessage(contents, roomUid)
            }

            suspend fun sendMessage (contents: String, roomUid: String) : Response<Int> {
                return _sendMessage(contents, roomUid)
            }

            private suspend fun _fetchAllMessagesOnce (roomUid: String) :Response<MutableList<MessageEntity>> {
                return repository.fetchAllMessagesOnce(roomUid)
            }

            suspend fun fetchAllMessagesOnce (roomUid: String) : Response<MutableList<MessageEntity>> {
                return _fetchAllMessagesOnce(roomUid)
            }
}