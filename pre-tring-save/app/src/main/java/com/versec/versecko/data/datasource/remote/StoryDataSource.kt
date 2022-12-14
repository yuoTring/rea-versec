package com.versec.versecko.data.datasource.remote

import android.net.Uri
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.util.Response

interface StoryDataSource {


    suspend fun uploadStory (story : StoryEntity) : Response<Int>
    suspend fun deleteStory (uid: String, size : Int) : Response<Int>

    suspend fun getStoryList (startTimestamp: Long, location : String?, depth : Int) : Response<MutableList<StoryEntity>>

    suspend fun getOwnStory (uid: String) : Response<StoryEntity>
    suspend fun getOwnStoryList () : Response<MutableList<StoryEntity>>

    suspend fun uploadImage (uriMap : MutableMap<String, Uri>, uid : String) : Response<Int>

    suspend fun likeStory (uid: String) : Response<StoryEntity>
    suspend fun cancelLike (uid: String) : Response<StoryEntity>

    suspend fun reportStory (uid: String) : Response<Int>


}