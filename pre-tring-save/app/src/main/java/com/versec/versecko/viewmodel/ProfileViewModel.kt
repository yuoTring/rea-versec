package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.StoryRepository
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.launch

class ProfileViewModel (

        private val userRepository: UserRepository,
        private val storyRepository: StoryRepository

        ) : ViewModel() {




        private val _userLocal : MutableLiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
        val userLocal : LiveData<UserEntity> = _userLocal


        var userEntity = UserEntity()


        private suspend fun _getOwnUser_Remote () : Response<UserEntity?> {
                return userRepository.getOwnUserOneShot()
        }

        suspend fun getOwnUser_Remote () : Response<UserEntity?> {
                return _getOwnUser_Remote()
        }

        private suspend fun _insertUser_Local (userEntity: UserEntity) : Response<Int> {
                return userRepository.insertUser_Local(userEntity)
        }

        suspend fun insertUser_Local (userEntity: UserEntity) : Response<Int> {
                return _insertUser_Local(userEntity)
        }

        private suspend fun _getOwnStories () : Response<MutableList<StoryEntity>> {

                return storyRepository.getOwnStoryList()
        }

        suspend fun getOwnStories () : Response<MutableList<StoryEntity>> {
                return _getOwnStories()
        }




}