package com.versec.versecko.data.entity

import java.io.Serializable

data class RoomEntity(

    var uid : String = "...",
    var members : MutableList<String> = mutableListOf(),
    var lastSent : String = "..."

) : Serializable
