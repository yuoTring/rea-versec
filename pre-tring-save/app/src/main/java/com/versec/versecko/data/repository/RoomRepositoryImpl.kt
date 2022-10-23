package com.versec.versecko.data.repository

import com.versec.versecko.data.datasource.remote.RoomDataSource
import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

class RoomRepositoryImpl (

    private val dataSource: RoomDataSource

        ) : RoomRepository {


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

    override suspend fun updateLastRead(roomUid: String): Response<Int> {
        return dataSource.updateLastRead(roomUid)
    }

    override suspend fun sendMessage(contents: String, roomUid: String): Response<Int> {
        return dataSource.sendMessage(contents, roomUid)
    }

    override fun observeMessage(roomUid: String): Flow<Map<Int, Response<MessageEntity>>> {
        return dataSource.observeMessage(roomUid)
    }

    override suspend fun readMessage(

        roomUid: String,
        messageUid : String

    ): Response<Int> {
        return dataSource.readMessage(roomUid, messageUid)
    }


    override fun observeOtherLastRead(roomUid: String, otherUid: String): Flow<Response<Long>> {
        return dataSource.observeOtherLastRead(roomUid, otherUid)
    }

    override suspend fun saveFCMToken(token: String): Response<Int> {
        return dataSource.saveFCMToken(token)
    }


}