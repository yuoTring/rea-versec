package com.versec.versecko.data.repository

import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun openChatRoom (otherUser : UserEntity, ownUser : UserEntity) : Response<Int>
    fun observeChatRoom () : Flow<Map<Int, Response<RoomEntity>>>
    fun deleteChatRoom (chatRoomUid: String, otherUid : String)

    suspend fun sendMessage (content : String, room : RoomEntity) : Response<Int>
    fun getMessages (chatRoomUid: String) : Flow<MessageEntity>

    suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
    suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>

}