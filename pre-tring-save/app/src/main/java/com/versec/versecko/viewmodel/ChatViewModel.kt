package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.*

class ChatViewModel (

    private val userRepository: UserRepository,
                repository: ChatRepository
        ) : ViewModel(){



    val likedUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(2).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val matchingUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(3).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val rooms : SharedFlow<Map<Int, Response<ChatRoomEntity>>> =
        repository.observeChatRoom().shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)



    private fun _setCounter (status: Int, count : Int) {
        userRepository.setCounter(status, count)
    }

    fun setCounter (status: Int, count: Int) {
        _setCounter(status, count)
    }

    private fun _getCounter (status : Int) : Int? {
        return userRepository.getCounter(status)
    }

    fun getCounter (status: Int) : Int? {
        return _getCounter(status)
    }


}

