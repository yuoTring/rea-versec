package com.versec.versecko.data.repository

import com.versec.versecko.data.datasource.remote.ChatDataSource
import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl (

    private val dataSource: ChatDataSource

        ) : ChatRepository {


    override suspend fun insertUser(member: RoomMemberEntity): Response<Int> {
        return dataSource.insertUser(member)
    }

    override suspend fun openRoom(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {
        return dataSource.openRoom(otherUser, ownUser)
    }

    override suspend fun getRoomForOneShot(roomUid: String): Response<RoomEntity?> {
        return dataSource.getRoomForOneShot(roomUid)
    }

    override fun observeRoomUid(): Flow<Map<Int, Response<Room>>> {
        return dataSource.observeRoomUid()
    }

    override fun observeRoom(roomUid: String): Flow<Response<RoomEntity>> {
        return dataSource.observeRoom(roomUid)
    }


    override fun deleteRoom(chatRoomUid: String, otherUid: String) {

        dataSource.deleteRoom(chatRoomUid, otherUid)
    }

    override suspend fun sendMessage(content: String, room: RoomEntity): Response<Int> {
        return dataSource.sendMessage(content, room)
    }


    override fun getMessages(chatRoomUid: String): Flow<MessageEntity> {

        return dataSource.getMessages(chatRoomUid)
    }

    override suspend fun resetUnreadMessageCounter(chatRoomUid: String): Response<Int> {
        return dataSource.resetUnreadMessageCounter(chatRoomUid)
    }

    override suspend fun resetReadOrNot(message: MessageEntity): Response<Int> {
        return dataSource.resetReadOrNot(message)
    }

    override suspend fun saveFCMToken(token: String): Response<Int> {
        return dataSource.saveFCMToken(token)
    }


}