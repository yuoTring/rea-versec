package com.versec.versecko.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class SettingViewModel (

    private val repository: UserRepository

        ) : ViewModel() {


            private val _user : MutableLiveData<UserEntity> = repository.getOwnUser_Local().asLiveData() as MutableLiveData<UserEntity>
            val user : LiveData<UserEntity> = _user

            private fun _logOut () : Response<Int> {
                return repository.logOut()
            }

            fun  logOut() : Response<Int> {
                return _logOut()
            }

            private suspend fun _inactivate() : Response<Int> {
                return repository.inactivateAccount()
            }

            suspend fun inactivateAccount () : Response<Int> {
                return _inactivate()
            }

            private suspend fun _activate() : Response<Int> {
                return repository.activateAccount()
            }

            private suspend fun _updateDeletedAt (activate : Boolean) : Response<Int> {
                return repository.updateDeletedAt(activate)
            }

            suspend fun updateDeletedAt (activate: Boolean) : Response<Int> {
                return _updateDeletedAt(activate)
            }

            suspend fun activate () : Response<Int> {
                return _activate()
            }

            private suspend fun _delete () : Response<Int> {
                return repository.deleteAccount()
            }

            suspend fun deleteAccount() : Response<Int> {
                return _delete()
            }

            private fun _getMatchingSetting () : Boolean {
                return repository.getMatchingNotification()
            }

            fun getMatchingSetting () : Boolean {
                return _getMatchingSetting()
            }

            private fun _getLikedSetting () : Boolean {
                return repository.getLikedNotification()
            }

            fun getLikedSetting () : Boolean {
                return _getLikedSetting()
            }

            private fun _getMessageSetting () : Boolean {
                return repository.getChatNotification()
            }

            fun getMessageSetting () : Boolean {
                return _getMessageSetting()
            }

            private fun _getMarketingNotificationStatus () : Boolean {
                return repository.getMarketingNotification()
            }

            fun getMarketingNotificationStatus () : Boolean {
                return _getMarketingNotificationStatus()
            }

            private fun _setMatching (on : Boolean) {
                repository.setMatchingNotification(on)
            }

            fun setMatching (on: Boolean) {
                _setMatching(on)
            }

            private fun _setLiked (on : Boolean) {
                repository.setLikedNotification(on)
            }

            fun setLiked (on : Boolean) {
                _setLiked(on)
            }

            private fun _setMessage (on: Boolean) {
                repository.setChatNotification(on)
            }

            fun setMessage (on: Boolean) {
                _setMessage(on)
            }

            private fun _setMarketing (on: Boolean) {
                repository.setMarketingNotification(on)
            }

            fun setMarketing (on: Boolean) {
                _setMarketing(on)
            }


}