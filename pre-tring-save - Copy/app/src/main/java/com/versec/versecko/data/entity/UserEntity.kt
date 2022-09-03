package com.versec.versecko.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "user")
data class UserEntity(

    @PrimaryKey var uid : String = "0",
    @ColumnInfo(name = "nick_name") var nickName : String = "ALexion",
    @ColumnInfo(name = "gender") var gender : String = "male",
    @ColumnInfo(name = "age") var age : Int =25,
    @ColumnInfo(name = "birth") var birth : String = "19991212",

    @ColumnInfo(name = "main_residence") var mainResidence : String = "Seoul",
    @ColumnInfo(name = "sub_residence") var subResidence : String = "seoul",
    @ColumnInfo(name = "trip_wish") var tripWish : MutableList<String> = mutableListOf("!!!","!!?"),
    @ColumnInfo(name = "trip_style") var tripStyle : MutableList<String> = mutableListOf("!!!","!!?"),
    @ColumnInfo(name = "self_introduction") var selfIntroduction : String = "Hi",

    //@ColumnInfo(name = "uri_list") var uriList : MutableList<String> = mutableListOf("!!!","!!?"),
    @ColumnInfo(name = "uri_Map") var uriMap : MutableMap<Int, String> = mutableMapOf(),
    @ColumnInfo(name = "geohash") var geohash : String = "",
    @ColumnInfo(name = "latitude") var latitude : Double = 37.75,
    @ColumnInfo(name = "longitude") var longitude : Double = 124.5,

    @ColumnInfo(name = "manner_score") var mannerScore : Double = 4.5,
    @ColumnInfo(name = "premium") var premiumOrNot : Boolean = false,
    @ColumnInfo(name = "knock") var knock : Int = 2,

    @ColumnInfo(name ="loungeStatus") var loungeStatus : Int = 0
    /**
     * 0 -> OwnUser
     * 1 -> users who I send a like to
     * 2 -> users who send me a like
     * 3 -> users who was matched with me
     */

)
