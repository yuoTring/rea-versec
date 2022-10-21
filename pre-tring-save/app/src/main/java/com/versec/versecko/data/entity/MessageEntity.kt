package com.versec.versecko.data.entity

data class MessageEntity(


    var uid : String = "...",
    var contents : String = "Hi this is error",

    var sender : String = "null",

    var timestamp : Long = 0,
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


