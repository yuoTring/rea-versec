package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.repository.StoryRepository
import com.versec.versecko.util.Response

class StoryUploadViewModel (

    private val repository: StoryRepository

        ) : ViewModel() {


            private suspend fun _uploadStory (story : StoryEntity) : Response<Int> {
                return repository.uploadStory(story)
            }

            suspend fun uploadStory (story: StoryEntity) : Response<Int> {
                return _uploadStory(story)
            }

}