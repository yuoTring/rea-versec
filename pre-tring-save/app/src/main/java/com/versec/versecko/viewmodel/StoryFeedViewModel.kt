package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.repository.StoryRepository
import com.versec.versecko.util.Response

class StoryFeedViewModel (

    private val storyRepository: StoryRepository

         ) : ViewModel() {



            private suspend fun _getStories (startTime : Long, place : String?, depth : Int) : Response<MutableList<StoryEntity>> {
                 return storyRepository.getStoryList(startTime, place, depth)
            }

            suspend fun getStories (startTime: Long, place: String?, depth: Int) : Response<MutableList<StoryEntity>> {
                return _getStories(startTime, place, depth)
            }



            private suspend fun _likeStory (uid : String) : Response<StoryEntity> {
                return storyRepository.likeStory(uid)
            }

            suspend fun likeStory(uid: String) : Response<StoryEntity> {
                return _likeStory(uid)
            }

            private suspend fun _cancelLike (uid: String) : Response<StoryEntity> {
                return storyRepository.cancelLike(uid)
            }

            suspend fun cancelLike (uid: String) : Response<StoryEntity> {
                return _cancelLike(uid)
            }



}