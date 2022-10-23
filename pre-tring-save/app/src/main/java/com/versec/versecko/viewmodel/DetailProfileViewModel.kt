package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.RoomRepository
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class DetailProfileViewModel (

    private val userRepository: UserRepository,
    private val repository: RoomRepository

        ) : ViewModel() {

    fun reject (otherUser: UserEntity) { _reject(otherUser) }
    private fun _reject (otherUser: UserEntity) { userRepository.skipUser(otherUser) }

    suspend fun rejectLiked (otherUser: UserEntity) : Response<Int> { return _rejectLiked(otherUser) }
    private suspend fun _rejectLiked (otherUser: UserEntity) : Response<Int> { return userRepository.rejectLiked(otherUser)}

    suspend fun rejectMatched (otherUser: UserEntity) : Response<Int> { return _rejectMatched(otherUser) }
    private suspend fun _rejectMatched (otherUser: UserEntity) :Response<Int> { return userRepository.rejectMatched(otherUser) }

    suspend fun like (otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return _like(otherUser, ownUser) }
    private suspend fun _like (otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return userRepository.likeUser(otherUser, ownUser) }

    suspend fun match(otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return _match(otherUser, ownUser) }
    private suspend fun _match(otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return userRepository.matchUser(otherUser, ownUser) }

    suspend fun openChat (otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return _openChat(otherUser, ownUser) }
    private suspend fun _openChat (otherUser: UserEntity, ownUser: UserEntity) : Response<Int> { return repository.openRoom(otherUser, ownUser) }

    suspend fun deleteMatch (otherUid : String) : Response<Int> { return _deleteMatch(otherUid) }
    private suspend fun _deleteMatch(otherUid: String) : Response<Int> { return userRepository.deleteMatch(otherUid) }


}