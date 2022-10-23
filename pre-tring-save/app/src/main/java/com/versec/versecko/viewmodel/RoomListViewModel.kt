package com.versec.versecko.viewmodel

import androidx.lifecycle.*
import com.versec.versecko.data.entity.*
import com.versec.versecko.data.repository.RoomRepository
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response
import kotlinx.coroutines.flow.*

class RoomListViewModel (

    private val userRepository: UserRepository,
    private val repository: RoomRepository

    ) : ViewModel(){



    val likedUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(2).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val matchingUsers : SharedFlow<Response<MutableList<UserEntity>>> =
        userRepository.getLoungeUsers(3).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val getRoomsUid : SharedFlow<Map<Int, Response<RoomInUser>>> =
        repository.observeRoomUid().shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)

    /**
    private fun _addListenerLastMessage (roomUid: String) : Flow<Response<String>> {

        val listener =
            repository.observeLastMessage(roomUid).
            shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)

        return listener
    }

    fun addListenerLastMessage (roomUid: String) : Flow<Response<String>> {
        return _addListenerLastMessage(roomUid)
    } **/

    private suspend fun _getUser (uid : String) : Response<RoomMemberEntity> {
        return repository.getUser(uid)
    }

    suspend fun getUser (uid: String) : Response<RoomMemberEntity> {
        return _getUser(uid)
    }



    private suspend fun _getOwnLastRead (roomUid: String) : Response<Long> {
        return repository.getOwnLastRead(roomUid)
    }

    suspend fun getOwnLastRead (roomUid: String) : Response<Long> {
        return _getOwnLastRead(roomUid)
    }



    private fun _addListenerLastMessage (roomUid: String) : LiveData<Response<String>> {
        return repository.observeLastMessage(roomUid).asLiveData()
    }

    fun addListenerLastMessage (roomUid: String) : LiveData<Response<String>> {
        return _addListenerLastMessage(roomUid)
    }



    fun getRoomUid () : Flow<Map<Int, Response<RoomInUser>>> {
        return repository.observeRoomUid().shareIn(viewModelScope, SharingStarted.WhileSubscribed(),0)
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



    suspend fun send (contents : String, roomUid: String) {

        repository.sendMessage(contents, roomUid)
    }

    suspend fun getLastMessage (roomUid: String) : Response<MessageEntity> {
        return repository.getLastMessage(roomUid)
    }




    private suspend fun _updateLastRead (roomUid: String) : Response<Int> {
        return repository.updateLastRead(roomUid)
    }

    suspend fun updateLastRead (roomUid: String) : Response<Int> {
        return _updateLastRead(roomUid)
    }




}

