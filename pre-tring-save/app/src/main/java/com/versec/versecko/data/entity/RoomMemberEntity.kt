package com.versec.versecko.data.entity

import java.io.Serializable
import java.util.*

data class RoomMemberEntity(

    var fcm : String = "null",

    var uid : String = "...",
    var nickName : String = "...",
    var profileUrl : String = "...",

    var rooms : MutableMap<String, RoomInUser> = mutableMapOf()



) : Serializable