package com.versec.versecko.data.datasource.remote

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File

class UserRemoteDataSourceImpl (

    private val auth : FirebaseAuth,
    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage

) : UserRemoteDataSource {

    override fun getOwnUser(): Flow<UserEntity> = callbackFlow{

        val document = fireStore.collection("database/user/userList/")
            .document("test!!!!!")

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

        Log.d("-----", userEntity.uid)

        fireStore.collection("database/user/userList/")
            .document(userEntity.uid)
            .set(userEntity)
        //.document(auth.uid.toString())
        //.set(userEntity)
    }

    override suspend fun signIn(credential: PhoneAuthCredential): Results<Int> {


        lateinit var signInResult : Results<Int>

        //1. sign in with phone number and sms code
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task ->

                if (task.isSuccessful) {


                }
                else {

                    signInResult = Results.Error(task.exception)
                }
            }.await()


        //2. check whether a user already signed up
        auth.currentUser?.let {
            fireStore.collection("database/user/userList")
                .document(it.uid)
                .get()
                .addOnCompleteListener { document ->
                    if (document.isSuccessful) {

                        if (document.getResult().exists()) {
                            signInResult = Results.Exist(2)
                        } else {
                            signInResult = Results.No(3)
                        }
                    } else {

                        signInResult = Results.Error(document.exception)

                    }
                }.await()
        }


        return signInResult
    }



    override suspend fun checkNickName(nickName: String): Results<Int> {


        lateinit var result : Results<Int>

        fireStore.collection("database")
            .document("user")
            .get()
            .addOnCompleteListener { document ->


                if (document.isSuccessful) {

                    val list = document.result.data?.get("nickNameList") as List<String>

                    if (list.contains(nickName))
                        result = Results.Exist(1)
                    else
                        result = Results.No(0)

                    //document.getResult().get("nickNameList")
                }
                else {

                }
            }.await()

        return result
    }

    override suspend fun getUsersWithGeoHash(
        latitude: Double,
        longitude: Double,
        radiusInMeter: Int
    ): List<UserEntity> {

        val center = GeoLocation(latitude, longitude)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter.toDouble())

        val tasks : MutableList<Task<QuerySnapshot>> = mutableListOf()

        val boundSize : Int = bounds.size

        var docs = mutableListOf<DocumentSnapshot>()
        var userList = mutableListOf<UserEntity>()

        bounds.forEach {

                geoQueryBounds ->

            val query = fireStore.collection("database/user/userList")
                .orderBy("geohash")
                .startAt(geoQueryBounds.startHash)
                .endAt(geoQueryBounds.endHash)

            tasks.add(query.get())
        }

        val tasksResult = Tasks.whenAllComplete(tasks).await()

        tasksResult.forEach { task ->

            val snapshot = task.result as QuerySnapshot

            snapshot.documents.forEach {

                docs.add(it)

            }
        }

        docs.forEach { documentSnapshot ->

            documentSnapshot.toObject(UserEntity::class.java)?.let { userList.add(it) }

        }

        return userList
    }


    override fun getUsers(
        latitude: Double,
        longitude: Double,
        radiusInMeter: Int

    ): Flow<MutableList<DocumentSnapshot>> = flow {


        val center = GeoLocation(latitude, longitude)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter.toDouble())

        val tasks : MutableList<Task<QuerySnapshot>> = mutableListOf()

        val boundSize : Int = bounds.size

        var docs = mutableListOf<DocumentSnapshot>()
        var userList = mutableListOf<UserEntity>()

        bounds.forEach {

                geoQueryBounds ->

            val query = fireStore.collection("database/user/userList")
                .orderBy("geohash")
                .startAt(geoQueryBounds.startHash)
                .endAt(geoQueryBounds.endHash)

            tasks.add(query.get())
        }


        val result = Tasks.whenAllComplete(tasks).await()

        result.forEach { task ->

            val snapshot = task.result as QuerySnapshot

            snapshot.documents

        }

    }


    override fun likeUser(userEntity: UserEntity, ownUser: UserEntity) {

        val uid = "test!!!!!"
        //ownUser.uid

        fireStore.collection("database/user/userList/"+uid+"/lounge/loungeHistory/likeUserList")
            .document(userEntity.uid)
            .set(userEntity)

        fireStore.collection("database/user/userList/"+userEntity.uid+"/lounge/loungeHistory/likedUserList")
            .document(uid)
            .set(ownUser)

        fireStore.collection("database/user/userList/"+userEntity.uid+"/lounge/loungeInformation/likedUserList")
            .document(uid)
            .set(ownUser)


        fireStore.collection("database/user/userList/"+userEntity.uid+"/lounge/loungeInformation")
            .document()
            .update("likedCounter", FieldValue.increment(1.0))







    }

    override fun skipUser(otherUserEntity: UserEntity, ownUser: UserEntity) {

        val uid = "test!!!!!"

        fireStore.collection("database/user/userList/"+uid+"lounge/loungeHistory/skipList")
            .document(otherUserEntity.uid)
            .set(otherUserEntity)
    }

    override suspend fun uploadImage (uriMap: MutableMap<String, Uri>)
    {


        var uploadMap = mutableMapOf<String, String>()

        uriMap.forEach { entry ->

            //val ref =
            //    storage.reference.child("images/profileImages/"+"uid"+"_"+)

            val ref =
                storage.reference.child("image/profileImages/"+"test!!!!!"+"/"+"test!!!!!"+"_"+entry.key)

            var uploadTask = ref.putFile(entry.value)

            uploadTask.addOnSuccessListener {


            }.addOnFailureListener {

            }.await()

        }

        uriMap.forEach { entry ->
            storage.reference.child("image/profileImages/"+"test!!!!!"+"/"+"test!!!!!"+"_"+entry.key)
                .downloadUrl.addOnSuccessListener {

                    uploadMap.put(entry.key, it.toString())

                    Log.d("uri-???", "key: "+ entry.key + "-----"+ it.toString())

                }.addOnFailureListener {

                }.await()
        }


        fireStore.collection("database/user/userList")
            .document("test!!!!!")
            .update("uriMap",uploadMap)









    }

    override suspend fun checkLoungeCount(status: Int, localCount: Int): Int {

        var count = 0

        var uid = "test!!!!!"

        fireStore.collection("database/user/userList/"+uid+"/lounge")
            .document("loungeInformation")
            .get()
            .addOnCompleteListener {

                task ->

                if (task.isSuccessful) {

                    val doc = task.result

                    count = doc.get("likedCounter").toString().toInt()
                    if (status == 3)
                        count = doc.get("matchedCounter").toString().toInt()


                    count = count - localCount



                }
                else {

                }
            }.await()

        return count
    }



    override suspend fun getNewLoungeUser(status: Int, newCount: Int): MutableList<UserEntity> {

        lateinit var newUserList : MutableList<UserEntity>

        val uid = "test!!!!!"

        var what= "likedUserList"
        if (status == 3)
            what = "matchUserList"

        fireStore.collection("database/user/userList/"+uid+"/lounge/loungeInformation/"+what)
            .limit(newCount.toLong())
            .get()
            .addOnCompleteListener { snap ->

                if (snap.isSuccessful) {

                    snap.result.documents.forEach { documentSnapshot ->
                        documentSnapshot.toObject(UserEntity::class.java)?.let { newUserList.add(it) }
                    }
                } else {

                }

            }.await()


        return newUserList


    }

    override fun updateLoungeUser(status: Int): Flow<MutableList<UserEntity>> = callbackFlow {

        lateinit var updatedUsers : MutableList<UserEntity>

        val uid = "test!!!!!"

        var what = "likedUserList"
        if (status == 3)
            what = "matchedUserList"

        val collection =
            fireStore.collection("database/user/userList/"+uid+"/lounge/loungeInformation/"+what)

        updatedUsers = mutableListOf()

        val subscription = collection.addSnapshotListener { value, error ->

            for (doc in value!!) {

                updatedUsers.add(doc.toObject(UserEntity::class.java))
            }

            trySend(updatedUsers)
        }

        awaitClose { subscription.remove() }

    }




}