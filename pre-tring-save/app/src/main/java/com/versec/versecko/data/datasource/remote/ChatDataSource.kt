package com.versec.versecko.data.datasource.remote

import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {


    suspend fun insertUser (member : RoomMemberEntity) : Response<Int>

    suspend fun openRoom(otherUser : UserEntity, ownUser : UserEntity) : Response<Int>
    suspend fun getRoomForOneShot (roomUid: String) : Response<RoomEntity?>

    fun observeRoomUid () : Flow<Map<Int, Response<Room>>>
    fun observeRoom (roomUid : String) : Flow<Response<RoomEntity>>

    fun deleteRoom (chatRoomUid: String, otherUid : String)

    suspend fun sendMessage (content : String, room : RoomEntity) : Response<Int>
    fun getMessages(chatRoomUid: String) : Flow<MessageEntity>
    suspend fun resetUnreadMessageCounter (chatRoomUid: String) : Response<Int>
    suspend fun resetReadOrNot (message: MessageEntity) : Response<Int>

    suspend fun saveFCMToken (token : String) : Response<Int>

}