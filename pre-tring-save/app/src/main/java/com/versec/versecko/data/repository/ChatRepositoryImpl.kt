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

    override suspend fun getUser(uid: String): Response<RoomMemberEntity> {
        return dataSource.getUser(uid)
    }

    override suspend fun openRoom(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {
        return dataSource.openRoom(otherUser, ownUser)
    }

    override suspend fun getRoomForOneShot(roomUid: String): Response<RoomEntity?> {
        return dataSource.getRoomForOneShot(roomUid)
    }

    override suspend fun getAllRoomUidForOneShot(): Response<MutableList<RoomInUser>> {
        return dataSource.getAllRoomUidForOneShot()
    }

    override suspend fun getLastMessage(roomUid: String): Response<MessageEntity> {
        return dataSource.getLastMessage(roomUid)
    }

    override fun observeRoomUid(): Flow<Map<Int, Response<RoomInUser>>> {
        return dataSource.observeRoomUid()
    }

    override fun observeLastMessage(roomUid: String): Flow<Response<String>> {
        return dataSource.observeLastMessage(roomUid)
    }

    override suspend fun getOwnLastRead(roomUid: String): Response<Long> {
        return dataSource.getOwnLastRead(roomUid)
    }


    override fun deleteRoom(chatRoomUid: String, otherUid: String) {
        dataSource.deleteRoom(chatRoomUid, otherUid)
    }

    override suspend fun sendMessage(contents: String, roomUid: String): Response<Int> {
        return dataSource.sendMessage(contents, roomUid)
    }

    override fun getMessage(roomUid: String): Flow<Response<MessageEntity>> {
        return dataSource.getMessage(roomUid)
    }


    override suspend fun resetUnreadMessageCounter(chatRoomUid: String): Response<Int> {
        return dataSource.resetUnreadMessageCounter(chatRoomUid)
    }

    override suspend fun resetReadOrNot(message: MessageEntity): Response<Int> {
        return dataSource.resetReadOrNot(message)
    }

    override fun observeOtherLastRead(roomUid: String, otherUid: String): Flow<Response<Long>> {
        return dataSource.observeOtherLastRead(roomUid, otherUid)
    }

    override suspend fun saveFCMToken(token: String): Response<Int> {
        return dataSource.saveFCMToken(token)
    }


}