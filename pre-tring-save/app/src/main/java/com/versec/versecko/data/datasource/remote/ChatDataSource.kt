package com.versec.versecko.data.datasource.remote

import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {


    suspend fun openChatRoom(otherUser : UserEntity, ownUser : UserEntity) : Response<Int>
    fun observeChatRoom () : Flow<Map<Int, Response<ChatRoomEntity>>>
    fun deleteChatRoom (chatRoomUid: String, otherUid : String)

    suspend fun sendMessage (content : String, room : ChatRoomEntity) : Response<Int>
    fun getMessages(chatRoomUid: String) : Flow<MessageEntity>
    suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
    suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>


}