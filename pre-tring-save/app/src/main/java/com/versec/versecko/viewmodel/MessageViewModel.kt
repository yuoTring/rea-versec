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



            private suspend fun _readMessages (roomUid : String, messageUid : String) : Response<Int> {
                return repository.readMessage(roomUid, messageUid)
            }

            suspend fun readMessage (roomUid: String, messageUid : String) : Response<Int> {
                return _readMessages(roomUid, messageUid)
            }




            private fun _observeMessage (roomUid: String) : Flow<Map<Int, Response<MessageEntity>>> {

                val flow =
                    repository.observeMessage(roomUid).shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)

                return flow
            }

            fun observeMessage (roomUid: String) : Flow<Map<Int, Response<MessageEntity>>> {
                return _observeMessage(roomUid)
            }



            private suspend fun _sendMessage (contents : String, roomUid: String) : Response<Int> {
                return repository.sendMessage(contents, roomUid)
            }

            suspend fun sendMessage (contents: String, roomUid: String) : Response<Int> {
                return _sendMessage(contents, roomUid)
            }








}