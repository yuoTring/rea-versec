package com.versec.versecko.data.datasource.remote

import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {


    suspend fun insertUser (member : RoomMemberEntity) : Response<Int>
    suspend fun getUser (uid : String) : Response<RoomMemberEntity>

    suspend fun openRoom(otherUser : UserEntity, ownUser : UserEntity) : Response<Int>
    suspend fun getRoomForOneShot (roomUid: String) : Response<RoomEntity?>

    suspend fun getAllRoomUidForOneShot () : Response<MutableList<RoomInUser>>

    suspend fun getLastMessage (roomUid: String) : Response<MessageEntity>

    fun observeRoomUid () : Flow<Map<Int, Response<RoomInUser>>>
    fun observeLastMessage (roomUid: String) : Flow<Response<String>>
    suspend fun getOwnLastRead (roomUid: String) : Response<Long>

    fun deleteRoom (chatRoomUid: String, otherUid : String)

    suspend fun sendMessage (contents : String, roomUid: String) : Response<Int>
    fun getMessage (roomUid: String) : Flow<Response<MessageEntity>>

    suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
    suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>

    fun observeOtherLastRead (roomUid: String, otherUid: String) : Flow<Response<Long>>

    suspend fun saveFCMToken (token : String) : Response<Int>

}