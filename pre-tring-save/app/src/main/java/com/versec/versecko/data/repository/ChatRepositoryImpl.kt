package com.versec.versecko.data.repository

import com.versec.versecko.data.datasource.remote.ChatDataSource
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl (

    private val chatDataSource: ChatDataSource

        ) : ChatRepository {
    override fun openChatRoom(otherUser: UserEntity, ownUser: UserEntity) {

        chatDataSource.openChatRoom(otherUser, ownUser)
    }

    override suspend fun getAllChatRooms(): MutableList<ChatRoomEntity> {

        return chatDataSource.getAllChatRooms()
    }

    override fun getUpdatedChatRoom(): Flow<ChatRoomEntity> {

        return chatDataSource.getUpdatedChatRoom()
    }

    override fun deleteChatRoom(chatRoomUid: String, otherUid: String) {

        chatDataSource.deleteChatRoom(chatRoomUid, otherUid)
    }

    override fun sendMessage(content: String, room: ChatRoomEntity): Results<Int> {

        return chatDataSource.sendMessage(content, room)
    }

    override suspend fun getAllMessages(chatRoomUid: String): MutableList<MessageEntity> {

        return chatDataSource.getAllMessages(chatRoomUid)
    }

    override fun getUpdatedMessage(chatRoomUid: String): Flow<MessageEntity> {

        return chatDataSource.getUpdatedMessage(chatRoomUid)
    }
}