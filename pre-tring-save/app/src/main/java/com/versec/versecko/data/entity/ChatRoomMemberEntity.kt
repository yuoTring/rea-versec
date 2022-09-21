package com.versec.versecko.data.entity

import java.io.Serializable

data class ChatRoomMemberEntity(

    var uid : String = "...",
    var nickName : String = "...",
    var uri : String = "..."

) : Serializable
