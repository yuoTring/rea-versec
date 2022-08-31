package com.versec.versecko.data.datasource.local

import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(

    private val appDatabase: AppDatabase

) : UserLocalDataSource {
    override fun getOwnUser(): Flow<UserEntity> {

        return appDatabase.userEntityDao().getOwnUser()

    }

    override fun getUserList(status: Int): Flow<MutableList<UserEntity>> {

        return appDatabase.userEntityDao().getUserList(status)
    }

    override suspend fun insertUser(userEntity: UserEntity) {

        appDatabase.userEntityDao().insertUser(userEntity)
    }


}