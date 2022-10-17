package com.versec.versecko.data.entity

import java.io.Serializable
import java.util.*

data class RoomMemberEntity(

    var fcm : String = "null",

    var uid : String = "...",
    var nickName : String = "...",
    var profileUrl : String = "...",

    var rooms : MutableList<Room> = mutableListOf()




) : Serializable

data class Room (
    var lastRead : Date = Date()
)