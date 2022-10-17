package com.versec.versecko.data.entity

import java.io.Serializable

data class RoomMemberEntity(

    var fcm : String = "null",

    var uid : String = "...",
    var nickName : String = "...",
    var profileUrl : String = "..."

) : Serializable
