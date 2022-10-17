package com.versec.versecko.data.repository

import android.net.Uri
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.util.Response

interface StoryRepository {

    suspend fun uploadStory (story : StoryEntity) : Response<Int>
    suspend fun editStory (story: StoryEntity) : Response<Int>
    suspend fun getStoryList (startUid : String, place : String?, depth : Int) : Response<MutableList<StoryEntity>>
    suspend fun uploadImage (uriMap : MutableMap<String, Uri>, uid : String) : Response<Int>

}