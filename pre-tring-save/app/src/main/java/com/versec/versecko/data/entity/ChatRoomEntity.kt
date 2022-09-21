package com.versec.versecko.data.entity

import java.io.Serializable

data class ChatRoomEntity(

    var chatRoomUid : String = "...",
    var memberMap : MutableMap<String, ChatRoomMemberEntity> = mutableMapOf(),
    var unreadMessageCounter : Int = 0,
    var lastMessageSent : String = "..."

) : Serializable
