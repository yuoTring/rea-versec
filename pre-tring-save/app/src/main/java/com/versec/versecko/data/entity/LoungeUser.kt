package com.versec.versecko.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

data class LoungeUser(

    var uid : String = "0",
    var nickName : String = "ALexion",
    var gender : String = "male",
    var birth : String = "19991212",

    var mainResidence : String = "Seoul",
    var subResidence : String = "seoul",
    var tripWish : MutableList<String> = mutableListOf(),
    var tripStyle : MutableList<String> = mutableListOf(),
    var selfIntroduction : String = "Hi",

    var uriMap : MutableMap<String,String> = mutableMapOf("0" to "---", "1" to "---"),
    var geohash : String = "???",
    var latitude : Double = 37.75,
    var longitude : Double = 124.5,
    var languages : MutableList<String> = mutableListOf("ko"),
    var deletedAt : Date? = null,

    var mannerScore : Double = 4.5,
    var premiumOrNot : Boolean = false,
    var knock : Int = 0,

    var loungeStatus : Int = 0,

    /**
     * 0 -> OwnUser
     * 1 -> users who I send a like to
     * 2 -> users who send me a like
     * 3 -> users who was matched with me
     */

    var phoneNumber : String = "010-0000-0000",

    var timestamp : Long = 0



) : Serializable
