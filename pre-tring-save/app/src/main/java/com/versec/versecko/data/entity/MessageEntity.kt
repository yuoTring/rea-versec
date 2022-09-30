package com.versec.versecko.data.entity

data class MessageEntity(


    var messageUid : String = "...",
    var contents : String = "Hi this is error",
    var chatRoomUid : String = "...",

    var sender : ChatRoomMemberEntity = ChatRoomMemberEntity(),
    var receiver : ChatRoomMemberEntity = ChatRoomMemberEntity(),

    var date : String = "10",
    var time : String ="15:15",
    var readed : Boolean = false
) {

    override fun equals(other: Any?): Boolean {

        val other = other as MessageEntity
        if (messageUid.equals(other.messageUid))
            return true
        else
            return false
    }
}


