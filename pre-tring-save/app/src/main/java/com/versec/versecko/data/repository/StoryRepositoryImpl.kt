package com.versec.versecko.data.repository

import android.net.Uri
import com.versec.versecko.data.datasource.remote.StoryDataSource
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.util.Response

class StoryRepositoryImpl (

        private val storyDataSource: StoryDataSource

        ) : StoryRepository {
        override suspend fun uploadStory(story: StoryEntity): Response<Int> {
                return storyDataSource.uploadStory(story)
        }

        override suspend fun editStory(story: StoryEntity): Response<Int> {
                return storyDataSource.editStory(story)
        }



        override suspend fun getStoryList(

                startUid: String,
                place: String?,
                depth: Int

        ): Response<MutableList<StoryEntity>> {

                return storyDataSource.getStoryList(startUid, place, depth)
        }

        override suspend fun uploadImage(

                uriMap: MutableMap<String, Uri>,
                uid: String

        ): Response<Int> {

                return storyDataSource.uploadImage(uriMap, uid)
        }


}