package com.versec.versecko.data.repository

import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun openChatRoom (otherUser : UserEntity, ownUser : UserEntity)
    suspend fun getAllChatRooms () : MutableList<ChatRoomEntity>
    fun getUpdatedChatRoom () : Flow<ChatRoomEntity>
    fun deleteChatRoom (chatRoomUid: String, otherUid : String)

    fun sendMessage (content : String, room : ChatRoomEntity) : Results<Int>
    suspend fun getAllMessages (chatRoomUid : String) : MutableList<MessageEntity>
    fun getUpdatedMessage (chatRoomUid: String) : Flow<MessageEntity>

}