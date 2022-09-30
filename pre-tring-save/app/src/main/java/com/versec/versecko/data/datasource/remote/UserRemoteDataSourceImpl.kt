package com.versec.versecko.data.datasource.remote

import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRemoteDataSourceImpl (

    private val auth : FirebaseAuth,
    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage

) : UserRemoteDataSource {

    companion object {
        val SUCCESS = 1
    }

    override fun getOwnUser(): Flow<UserEntity> = callbackFlow{

        val document = fireStore.collection("database/user/userList/")
            .document(AppContext.uid)

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


    override suspend fun insertUser(userEntity: UserEntity) {

        Log.d("user-get", userEntity.uid)

        fireStore.collection("database/user/userList/")
            .document(userEntity.uid)
            .set(userEntity)
        //.document(auth.uid.toString())
        //.set(userEntity)
    }

    override suspend fun signIn(credential: PhoneAuthCredential): Response<Int> {


        lateinit var signInResult : Response<Int>

        //1. sign in with phone number and sms code
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                    task ->

                if (task.isSuccessful) {

                    AppContext.uid = task.result.user!!.uid

                }
                else {

                    signInResult = Response.Error(task.exception?.message.toString())

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
                            signInResult = Response.Exist(2)
                        } else {
                            signInResult = Response.No(3)
                        }
                    } else {

                        signInResult = Response.Error(document.exception?.message.toString())

                    }
                }.await()
        }


        return signInResult
    }



    override suspend fun checkNickName(nickName: String): Response<Int> {


        lateinit var result : Response<Int>

        fireStore.collection("database")
            .document("user")
            .get()
            .addOnCompleteListener { document ->


                if (document.isSuccessful) {

                    val list = document.result.data?.get("nickNameList") as List<String>

                    if (list.contains(nickName))
                        result = Response.Exist(1)
                    else
                        result = Response.No(0)

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
        radiusInMeter: Int,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {



        try {

            val center = GeoLocation(latitude, longitude)

            val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter.toDouble())

            val tasks : MutableList<Task<QuerySnapshot>> = mutableListOf()


            var docs = mutableListOf<DocumentSnapshot>()
            var userList = mutableListOf<UserEntity>()

            bounds.forEach {

                    geoQueryBounds ->

                val query = fireStore.collection("database/user/userList")
                    .orderBy("geohash")
                    .startAt(geoQueryBounds.startHash)
                    .endAt(geoQueryBounds.endHash)

                if (!gender.equals("both")) {
                  query.whereEqualTo("gender", gender)
                }

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

                val user = documentSnapshot.toObject(UserEntity::class.java)!!

                if (user.age>=minAge && user.age<maxAge) {
                    userList.add(user)
                }
            }


            /**
            fireStore.collection("database/user/userList/")
            .document("testestestuiduiduid_____")
            .get()
            .addOnCompleteListener {

            userList.add(0, it.result.toObject(UserEntity::class.java)!!)
            }.await() **/


            val user = fireStore.collection("database/user/userList/")
                .document("test!!!!!")
                .get()
                .await()
                .toObject(UserEntity::class.java)

            userList.add(user!!)

            return Response.Success(userList)


        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun getUsersWithCondition(conditions: List<String>): Response<MutableList<UserEntity>> {

        try {



        } catch (exception : Exception) {

        }
        TODO("Not yet implemented")
    }

    override suspend fun likeUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {

        val uid = AppContext.uid

        ownUser.loungeStatus = 2
        otherUser.loungeStatus =1

        Log.d("like-check", "other: "+ otherUser.uid)
        Log.d("like-check", "own: "+ ownUser.uid)



        try {
            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeInformation/likedUserList")
                .document(uid)
                .set(ownUser)
                .await()

                likeHistory(otherUser, ownUser)
            return Response.Success(SUCCESS)
        }
        catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    private fun likeHistory(otherUser: UserEntity, ownUser: UserEntity) {

        fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeHistory/likeUserList")
            .document(otherUser.uid)
            .set(otherUser)

        fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeHistory/likedUserList")
            .document(AppContext.uid)
            .set(ownUser)
    }

    override fun skipUser(otherUser: UserEntity) {

        try {

            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeHistory/skipUserList")
                .document(otherUser.uid)
                .set(otherUser)
        }
        catch (exception : Exception) {

        }

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


                }.addOnFailureListener {

                }.await()
        }

        for (index in uriMap.size .. 5) { uploadMap.put(index.toString(), "null") }


        fireStore.collection("database/user/userList")
            .document("test!!!!!")
            .update("uriMap",uploadMap)









    }

    override fun deleteImage(index: Int) {

        val uid = AppContext.uid

        storage.reference.child("image/profileImages/"+uid+"/"+uid+"_"+index).delete()

        fireStore.collection("database/user/userList/")
            .document(uid)
            .update(mapOf(

                "uriMap.${index.toString()}" to "null"
            ))
    }

    override suspend fun reuploadImage(index: Int, uri: Uri) {

        val uid = "test!!!!!"


        val ref = storage.reference.child("image/profileImages/"+uid+"/"+uid+"_"+index)

        val uploadTask = ref.putFile(uri)

        uploadTask.addOnSuccessListener { }.addOnFailureListener {  }.await()

        var uri= "null"

        ref.downloadUrl.addOnSuccessListener {
            uri = it.toString()
        }.addOnFailureListener {  }.await()

        fireStore.collection("database/user/userList")
            .document(uid)
            .update(mapOf(
                "uriMap.${index.toString()}" to uri
            ))
    }

    override fun getLoungeUsers(status: Int): Flow<Response<MutableList<UserEntity>>> = callbackFlow {

        val uid = AppContext.uid

        lateinit var subscription : ListenerRegistration

        try {

            trySend(Response.Loading)

            if (status == 2) {


                subscription =
                fireStore.collection("database/user/userList/"+uid+"/lounge/loungeInformation/likedUserList")
                    .addSnapshotListener { value, error ->

                        if (value != null) {

                            trySend(Response.Success(value.toObjects(UserEntity::class.java)))
                        }

                    }


            } else {

                subscription =
                    fireStore.collection("database/user/userList/"+uid+"/lounge/loungeInformation/matchingUserList")
                        .addSnapshotListener { value, error ->

                            if (value != null) {

                                trySend(Response.Success(value.toObjects(UserEntity::class.java)))
                            }

                        }

            }

        } catch (exception : Exception) {
            trySend(Response.Error(exception.message.toString()))
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun matchUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {

        Log.d("match-check", "other: "+ otherUser.uid)
        Log.d("match-check", "own: "+ownUser.uid)

        try {

            otherUser.loungeStatus = 3
            ownUser.loungeStatus = 3

            fireStore.collection("database/user/userList/"+ownUser.uid+"/lounge/loungeInformation/likedUserList")
                .document(otherUser.uid)
                .delete()
                .await()


            fireStore.collection("database/user/userList/"+ownUser.uid+"/lounge/loungeInformation/matchingUserList")
                .document(otherUser.uid)
                .set(otherUser)
                .await()

            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeInformation/matchingUserList")
                .document(ownUser.uid)
                .set(ownUser)
                .await()

            matchHistory(otherUser, ownUser)

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }



    }

    override suspend fun deleteMatch(otherUid: String): Response<Int> {

        try {


            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeInformation/matchingUserList/")
                .document(otherUid)
                .delete()
                .await()

            fireStore.collection("database/user/userList/"+otherUid+"/lounge/loungeInformation/matchingUserList/")
                .document(AppContext.uid)
                .delete()
                .await()

            return Response.Success(SUCCESS)

        }
        catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    private fun matchHistory (otherUser: UserEntity, ownUser: UserEntity) {

        try {

            fireStore.collection("database/user/userList/"+ownUser.uid+"/lounge/loungeHistory/matchingUserList")
                .document(otherUser.uid)
                .set(otherUser)

            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeHistory/matchingUserList")
                .document(ownUser.uid)
                .set(ownUser)

            fireStore.collection("database/user/userList/"+ownUser.uid+"/lounge/loungeHistory/likeUserList")
                .document(otherUser.uid)
                .set(otherUser)

            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeHistory/matchingUserList")
                .document(ownUser.uid)
                .set(ownUser)


        } catch (exception : Exception) {

        }
    }

    override suspend fun rejectLiked(otherUser: UserEntity): Response<Int> {

        try {

            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeInformation/matchingUserList")
                .document(otherUser.uid)
                .delete()
                .await()

            skipUser(otherUser)

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }
    }



    override suspend fun rejectMatched(otherUser: UserEntity): Response<Int> {

        try {

            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeInformation/matchingUserList")
                .document(otherUser.uid)
                .delete()
                .await()

            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeInformation/matchingUserList")
                .document(AppContext.uid)
                .delete()
                .await()

            skipUser(otherUser)

            return Response.Success(SUCCESS)


        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }

    }







}