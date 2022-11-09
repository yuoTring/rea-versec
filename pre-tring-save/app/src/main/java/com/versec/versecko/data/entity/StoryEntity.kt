package com.versec.versecko.data.entity

import java.io.Serializable

data class StoryEntity(

    var uid : String = "uid",
    var userUid : String ="uid",
    var userNickName : String ="???",

    var contents : String = "~~~",
    var uriMap : MutableMap<String,String> = mutableMapOf(),

    var mainLocation : String = "???",
    var subLocation : String = "???",
    var likes : MutableList<String> = mutableListOf(),

    var timestamp : Long = System.currentTimeMillis()

) : Serializable
