package com.versec.versecko.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.versec.domain.model.UserModel
import com.versec.domain.repository.UserRepositoryDepre
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel (
    //var getUserUseCase: GetUserUseCase ,
    //var insertUserUseCase: InsertUserUseCase

    private val userRepository : UserRepository

): ViewModel()
{



    val _user : LiveData<UserEntity> = userRepository.getOwnUser_Local().asLiveData()
        //userRepository.getUserList_Local(0).asLiveData()

        /**liveData {

        val data = userRepository.getUserList_Local(0)

        Log.d("room-db-livedata", "livedata: "+ data.size)

        emit(data)
        //val data = getUserUseCase.invoke()

        //emit(data)

    }**/



    //private val _userList = MutableLiveData<List<UserModel>>()


    fun insertUser (userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            //insertUserUseCase.invoke(userModel)

            Log.d("room-db-livedata", "insert : livedata")

            //userRepository.insertUser_Local(userEntity)
            userRepository.insertUser_Local(userEntity)
        }
    }



}