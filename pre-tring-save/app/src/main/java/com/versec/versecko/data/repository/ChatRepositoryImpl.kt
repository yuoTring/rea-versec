package com.versec.versecko.data.repository

import com.versec.versecko.data.datasource.remote.ChatDataSource
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl (

    private val chatDataSource: ChatDataSource

        ) : ChatRepository {
    override suspend fun openChatRoom(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {
        return chatDataSource.openChatRoom(otherUser, ownUser)
    }

    override fun observeChatRoom(): Flow<Map<Int, Response<ChatRoomEntity>>> {
        return chatDataSource.observeChatRoom()
    }


    override fun deleteChatRoom(chatRoomUid: String, otherUid: String) {

        chatDataSource.deleteChatRoom(chatRoomUid, otherUid)
    }

    override suspend fun sendMessage(content: String, room: ChatRoomEntity): Response<Int> {
        return chatDataSource.sendMessage(content, room)
    }


    override fun getMessages(chatRoomUid: String): Flow<MessageEntity> {

        return chatDataSource.getMessages(chatRoomUid)
    }

    override suspend fun resetUnreadMessageCounter(chatRoomUid: String): Response<Int> {
        return chatDataSource.resetUnreadMessageCounter(chatRoomUid)
    }

    override suspend fun resetReadOrNot(message: MessageEntity): Response<Int> {
        return chatDataSource.resetReadOrNot(message)
    }


}