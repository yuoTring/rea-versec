package com.versec.versecko.data.datasource.remote

import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface RoomDataSource {


    suspend fun insertUser (member : RoomMemberEntity) : Response<Int>
    suspend fun getUser (uid : String) : Response<RoomMemberEntity>

    suspend fun openRoom(otherUser : UserEntity, ownUser : UserEntity) : Response<Int>
    suspend fun getRoomForOneShot (roomUid: String) : Response<RoomEntity?>

    suspend fun getAllRoomUidForOneShot () : Response<MutableList<RoomInUser>>

    suspend fun getLastMessage (roomUid: String) : Response<MessageEntity>

    fun observeRoomUid () : Flow<Map<Int, Response<RoomInUser>>>
    fun observeLastMessage (roomUid: String) : Flow<Response<String>>

    suspend fun getOwnLastRead (roomUid: String) : Response<Long>
    suspend fun updateLastRead (roomUid: String) : Response<Int>

    suspend fun sendMessage (contents : String, roomUid: String) : Response<Int>
    fun observeMessage (roomUid: String) : Flow<Map<Int, Response<MessageEntity>>>
    suspend fun readMessage (roomUid: String, messageUid : String) : Response<Int>

    fun observeOtherLastRead (roomUid: String, otherUid: String) : Flow<Response<Long>>

    suspend fun saveFCMToken (token : String) : Response<Int>

}