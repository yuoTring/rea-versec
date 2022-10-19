package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.Room
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.*

class RoomListViewModel (

    private val userRepository: UserRepository,
    private val repository: ChatRepository

    ) : ViewModel(){



    val likedUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(2).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val matchingUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(3).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val roomsUid : SharedFlow<Map<Int, Response<Room>>> =
        repository.observeRoomUid().shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)


    fun addListenerToRoom (roomUid : String) : SharedFlow<Response<RoomEntity>> {

        val room : SharedFlow<Response<RoomEntity>>
        = repository.observeRoom(roomUid).shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)


        return room
    }

    private suspend fun _getRoomForOneShot (roomUid: String) : Response<RoomEntity?> {
        return repository.getRoomForOneShot(roomUid)
    }

    suspend fun getRoomForOneShot (roomUid: String) : Response<RoomEntity?> {
        return _getRoomForOneShot(roomUid)
    }

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

