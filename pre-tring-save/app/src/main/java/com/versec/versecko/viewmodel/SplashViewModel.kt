package com.versec.versecko.viewmodel

import androidx.lifecycle.ViewModel
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.util.Response

class SplashViewModel (

    private val repository: UserRepository

        ) : ViewModel() {




            private fun _getUid () : Response<String?> {
                return repository.getUid()
            }

            fun getUid () : Response<String?> {
                return _getUid()
            }

            private suspend fun _checkUid (uid : String) : Response<Int> {
                return repository.checkUid(uid)
            }

            suspend fun checkUid (uid: String) : Response<Int> {
                return _checkUid(uid)
            }



}