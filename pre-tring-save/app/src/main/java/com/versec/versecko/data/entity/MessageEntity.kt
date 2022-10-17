package com.versec.versecko.data.entity

data class MessageEntity(


    var uid : String = "...",
    var contents : String = "Hi this is error",

    var sender : RoomMemberEntity = RoomMemberEntity(),

    var date : String = "10",
    var time : String ="15:15",
    var read : Boolean = false

) {

    override fun equals(other: Any?): Boolean {

        val other = other as MessageEntity
        if (uid.equals(other.uid))
            return true
        else
            return false
    }
}


