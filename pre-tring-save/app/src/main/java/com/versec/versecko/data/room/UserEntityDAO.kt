package com.versec.versecko.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.versec.versecko.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEntityDAO {

    /**
    @Query ("SELECT * FROM user " )
    fun getAllUser() : List<UserEntity>
    //List<UserEntity>

    @Query ("SELECT * FROM user WHERE uid = (:uid) ")
    fun getUser(uid : String) : UserEntity

    @Insert
    fun insertUser (user: UserEntity)

    @Query("DELETE FROM user WHERE uid = (:uid)")
    fun delete(uid : String) **/

    /**

    @Query ("SELECT * FROM user " )
    suspend fun getAllUser() : MutableList<UserEntity>

    @Query ("SELECT * FROM user WHERE uid = (:uid) ")
    suspend fun getUser(uid: String) :UserEntity

    @Insert (onConflict = REPLACE)
    suspend fun insertUser (user : UserEntity)

    @Query("DELETE FROM user WHERE uid = (:uid)")
    suspend fun delete(uid : String) **/

    @Query ("SELECT * FROM user WHERE loungeStatus = 0")
    fun getOwnUser () : Flow<UserEntity>

    @Query ("SELECT * FROM user WHERE loungeStatus = (:status)")
    fun getUserList (status: Int) :Flow<MutableList<UserEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertUser (userEntity: UserEntity)

    @Query("UPDATE user SET uri_Map= :uri_Map WHERE loungeStatus = :status")
    suspend fun updateUriList (uri_Map : MutableMap<String,String>, status: Int)



}