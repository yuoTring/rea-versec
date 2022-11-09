package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.repository.StoryRepository
import com.versec.versecko.util.Response

class StoryDetailViewModel (

    private val storyRepository: StoryRepository
        ) : ViewModel() {


    private suspend fun _getOwnStory (uid: String) : Response<StoryEntity> {
        return storyRepository.getOwnStory(uid)
    }

    suspend fun getOwnStory (uid: String) : Response<StoryEntity> {
        return _getOwnStory(uid)
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


    private suspend fun _deleteStory (uid: String, size: Int) : Response<Int> {
        return storyRepository.deleteStory(uid, size)
    }

    suspend fun deleteStory (uid: String, size : Int) : Response<Int> {
        return _deleteStory(uid, size)
    }

}