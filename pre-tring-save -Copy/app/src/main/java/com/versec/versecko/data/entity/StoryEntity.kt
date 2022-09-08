package com.versec.versecko.data.entity

data class StoryEntity(

    var uid : String,
    var userUid : String,
    var userNickName : String,

    var contents : String,
    var uriList : List<String>,

    var location : String,
    var likes : Int

)
