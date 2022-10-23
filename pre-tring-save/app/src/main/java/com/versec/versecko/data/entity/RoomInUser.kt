package com.versec.versecko.data.entity

import java.io.Serializable

data class RoomInUser(

    var uid : String = "null",
    var lastRead : Long = 0

) : Serializable
