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

        override suspend fun deleteStory(uid: String, size: Int): Response<Int> {
                return storyDataSource.deleteStory(uid, size)
        }


        override suspend fun getStoryList(
                startTimestamp: Long,
                place: String?,
                depth: Int
        ): Response<MutableList<StoryEntity>> {

                return storyDataSource.getStoryList(startTimestamp, place, depth)
        }

        override suspend fun getOwnStory(uid: String): Response<StoryEntity> {

                return storyDataSource.getOwnStory(uid)
        }


        override suspend fun getOwnStoryList(): Response<MutableList<StoryEntity>> {

                return storyDataSource.getOwnStoryList()
        }

        override suspend fun uploadImage(

                uriMap: MutableMap<String, Uri>,
                uid: String

        ): Response<Int> {

                return storyDataSource.uploadImage(uriMap, uid)
        }

        override suspend fun likeStory(uid: String): Response<StoryEntity> {
                return storyDataSource.likeStory(uid)
        }

        override suspend fun cancelLike(uid: String): Response<StoryEntity> {
                return storyDataSource.cancelLike(uid)
        }

        override suspend fun reportStory(uid: String): Response<Int> {

                return storyDataSource.reportStory(uid)
        }


}