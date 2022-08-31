package com.versec.domain.model

data class StoryModel(

    var uid : String,
    var userUid : String,
    var userNickName : String,

    var contents : String,
    var uriList : List<String>,

    var location : String,
    var likes : Int

)
