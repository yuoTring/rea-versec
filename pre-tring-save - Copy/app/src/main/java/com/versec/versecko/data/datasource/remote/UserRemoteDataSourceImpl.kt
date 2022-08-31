package com.versec.versecko.data.datasource.remote

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRemoteDataSourceImpl (

    private val auth : FirebaseAuth,
    private val fireStore : FirebaseFirestore

): UserRemoteDataSource {

    override fun getOwnUser(): Flow<UserEntity>  = callbackFlow{

        val document = fireStore.collection("database/user/userList/")
            .document("testestestuiduiduid_____")

        val subscription = document.addSnapshotListener { snapshot,_ ->

            if (snapshot!!.exists()) {

                val ownUser = snapshot.toObject<UserEntity>()

                if (ownUser != null) {
                    trySend(ownUser)
                }
            }
        }

        awaitClose { subscription.remove() }

    }

    override fun getLoungeUserList(status: Int): Flow<MutableList<UserEntity>> {
        TODO("Not yet implemented")
    }


    override suspend fun insertUser(userEntity: UserEntity) {

        fireStore.collection("database/user/userList/")
            .document(userEntity.uid)
            .set(userEntity)
            //.document(auth.uid.toString())
            //.set(userEntity)
    }





    override fun signIn(credential: PhoneAuthCredential): Flow<Results<Int>> = callbackFlow {


        val signInRequest = auth.signInWithCredential(credential)

        signInRequest.addOnCompleteListener { task ->

            if (task.isSuccessful) {

                Log.d("auth-firebase", task.getResult().user?.uid.toString())

                trySend(Results.Success(1))
            }
            else {
                Log.d("auth-firebase", "fail")
                trySend(Results.Error(task.exception))

            }

        }



    }


}