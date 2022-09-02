package com.versec.domain.model

data class UserModel(
    var uid : String = "000",
    var nickName : String = "Alexar",
    var gender : String = "female",
    var age : Int = 22,
    var birth : String ="19990901",

    var mainResidence : String = "Seoul",
    var subResidence : String = "???",
    var tripWish : MutableList<String> = mutableListOf("!!!","!!?"),
    var tripStyle : MutableList<String> = mutableListOf("!!!","!!?"),
    var selfIntroduction : String = "hi -_-",

    var uriList : MutableList<String> = mutableListOf("!!!","!!?"),
    var geohash : String = "none",
    var latitude : Double = 37.455,
    var longitude : Double = 124.890,

    var mannerScore : Double = 4.5,

    var premiumOrNot : Boolean = false,
    var knock : Int = 0
)
