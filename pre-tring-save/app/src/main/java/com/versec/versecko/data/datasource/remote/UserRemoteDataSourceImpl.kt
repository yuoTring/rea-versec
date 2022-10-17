package com.versec.versecko.data.datasource.remote

import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class UserRemoteDataSourceImpl (

    private val auth : FirebaseAuth,
    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage,
    private val firebaseMessaging: FirebaseMessaging

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

    override suspend fun getOwnUserOneShot(): Response<UserEntity?> {

        try {

            val user = fireStore.collection("database/user/userList/")
                .document(AppContext.uid)
                .get()
                .await().toObject(UserEntity::class.java)


            return Response.Success(user)
        }
        catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun insertUser(userEntity: UserEntity): Response<Int> {

        try {

            fireStore.collection("database/user/userList/")
                .document(userEntity.uid)
                .set(userEntity)
                .await()

            val task = fireStore.collection("database")
                .document("user")
                .get()
                .await()

            val nickNameList : MutableList<String> = task.get("nickNameList") as MutableList<String>

            nickNameList.add(userEntity.nickName)

            fireStore.collection("database")
                .document("user")
                .update("nickNameList", nickNameList)
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }


    override suspend fun signIn(credential: PhoneAuthCredential): Response<Int> {

        /**
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
        }**/

        try {

            val authResult = auth.signInWithCredential(credential).await()

            AppContext.uid = authResult.user!!.uid


            return checkUid(AppContext.uid)





        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }


    }

    override fun getUid(): Response<String?> {

        val user = auth.currentUser

        if (user != null)
            return Response.Exist(user.uid)
        else
            return Response.No(null)
    }

    override suspend fun checkUid(uid: String): Response<Int> {

        try {


            val size = fireStore.collection("database/user/userList")
                    .whereEqualTo("uid", uid)
                    .get()
                    .await()
                    .size()

            if (size == 0)
                return Response.No(size)
            else
                return Response.Exist(size)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

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

                var query = fireStore.collection("database/user/userList")
                    .orderBy("geohash")
                    .whereEqualTo("gender", gender)
                    .startAt(geoQueryBounds.startHash)
                    .endAt(geoQueryBounds.endHash)

                if (gender.equals("both")) {

                    query =
                        fireStore.collection("database/user/userList")
                            .orderBy("geohash")
                            .startAt(geoQueryBounds.startHash)
                            .endAt(geoQueryBounds.endHash)
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

                val age = calAgeRange(user.birth.substring(0,4))


                if (age >= minAge && age <= maxAge)
                    if (!user.uid.equals(AppContext.uid))
                        userList.add(user)

            }






            return Response.Success(userList)


        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun getUsersWithPlace(
        places: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        try {

            val minYear = Calendar.getInstance().get(Calendar.YEAR) - minAge
            val maxYear = Calendar.getInstance().get(Calendar.YEAR) - maxAge

            val ref = fireStore.collection("database/user/userList/")


            var querySnapshot =
                ref
                    .whereArrayContainsAny("tripWish", places)
                    .whereEqualTo("gender", gender)
                    .whereGreaterThanOrEqualTo("birth", maxYear.toString()+"0000")
                    .whereLessThanOrEqualTo("birth", minYear.toString()+"0000")
                    .get()
                    .await()

            if (gender == "both") {

                querySnapshot =
                    ref
                        .whereArrayContainsAny("tripWish", places)
                        .whereGreaterThanOrEqualTo("age", minAge)
                        .whereLessThanOrEqualTo("age", maxAge)
                        .get()
                        .await()
            }



            val users = mutableListOf<UserEntity>()

            querySnapshot.documents.forEach { documentSnapshot ->
                documentSnapshot.toObject(UserEntity::class.java)?.let { users.add(it) }
            }

            return Response.Success(users)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }


    override suspend fun getUsersWithResidence(
        residences: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        try {

            val minYear = Calendar.getInstance().get(Calendar.YEAR) - minAge
            val maxYear = Calendar.getInstance().get(Calendar.YEAR) - maxAge

            val ref = fireStore.collection("database/user/userList/")

            val querySnapshot =
                    ref
                        .whereIn("subResidence", residences)
                        .whereEqualTo("gender", gender)
                        .whereGreaterThanOrEqualTo("birth", maxYear.toString()+"0000")
                        .whereLessThanOrEqualTo("birth", minYear.toString()+"0000")
                        .get()
                        .await()

            val users = mutableListOf<UserEntity>()

            querySnapshot.documents.forEach { documentSnapshot ->
                documentSnapshot.toObject(UserEntity::class.java)?.let { users.add(it) }
            }

            return Response.Success(users)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun getUsersWithStyle(
        styles: List<String>,
        gender: String,
        minAge: Int,
        maxAge: Int
    ): Response<MutableList<UserEntity>> {

        try {

            val minYear = Calendar.getInstance().get(Calendar.YEAR) - minAge
            val maxYear = Calendar.getInstance().get(Calendar.YEAR) - maxAge

            val ref = fireStore.collection("database/user/userList/")

            val querySnapshot =
                ref
                    .whereArrayContainsAny("tripStyle", styles)
                    .whereEqualTo("gender", gender)
                    .whereGreaterThanOrEqualTo("birth", maxYear.toString()+"0000")
                    .whereLessThanOrEqualTo("birth", minYear.toString()+"0000")
                    .get()
                    .await()

            val users = mutableListOf<UserEntity>()

            querySnapshot.documents.forEach { documentSnapshot ->
                documentSnapshot.toObject(UserEntity::class.java)?.let { users.add(it) }
            }

            return Response.Success(users)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }


    override suspend fun likeUser(otherUser: UserEntity, ownUser: UserEntity): Response<Int> {

        val uid = AppContext.uid

        ownUser.loungeStatus = 2
        otherUser.loungeStatus =1

        try {
            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeInformation/likedUserList")
                .document(uid)
                .set(mapOf("uid" to uid))
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
                .set(mapOf("uid" to otherUser.uid))
        }
        catch (exception : Exception) {

        }

    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>): Response<Int> {

        try {

            val uploadMap = mutableMapOf<String,String>()


            uriMap.forEach { entry ->

                val ref =
                    storage.reference.child("image/profileImages/"+AppContext.uid+"/"+AppContext.uid+"_"+entry.key)



                ref.putFile(entry.value).await()

            }

            uriMap.forEach { entry ->


                val url =
                    storage.reference.child("image/profileImages/"+AppContext.uid+"/"+AppContext.uid+"_"+entry.key).downloadUrl.await()


                uploadMap.put(entry.key, url.toString())
            }

            for (index in uriMap.size .. 5) { uploadMap.put(index.toString(), "null") }


            fireStore.collection("database/user/userList/")
                .document(AppContext.uid)
                .update("uriMap", uploadMap)
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun deleteImage(index: Int): Response<Int> {

        try {

            val uid = AppContext.uid

            storage.reference.child("image/profileImages/"+uid+"/"+uid+"_"+index).delete().await()

            fireStore.collection("database/user/userList/")
                .document(uid)
                .update(mapOf(

                    "uriMap.${index.toString()}" to "null"

                )).await()


            return Response.Success(SUCCESS)
        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }


    override suspend fun reuploadImage(index: Int, uri: Uri): Response<Int> {

        try {

            val uid = AppContext.uid

            val ref = storage.reference.child("image/profileImages/"+uid+"/"+uid+"_"+index)


            ref.putFile(uri).await()


            var uri= ref.downloadUrl.await()

            fireStore.collection("database/user/userList")
                .document(uid)
                .update(mapOf(
                    "uriMap.${index.toString()}" to uri
                )).await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
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

                            val documentList = value.toObjects(UserEntity::class.java)

                            val userList = mutableListOf<UserEntity>()

                            CoroutineScope(Dispatchers.IO).launch {

                                documentList.forEach { user->

                                    val result = fireStore.collection("database/user/userList")
                                        .document(user.uid)
                                        .get()
                                        .await()

                                    if (result.exists()) {

                                        val userEntity = result.toObject(UserEntity::class.java)!!
                                        userEntity.loungeStatus = 2

                                        userList.add(userEntity)

                                    } else {
                                         Log.d("liked-null", "null")
                                    }

                                }

                                trySend(Response.Success(userList))

                            }


                        }

                    }


            } else {

                subscription =
                    fireStore.collection("database/user/userList/"+uid+"/lounge/loungeInformation/matchingUserList")
                        .addSnapshotListener { value, error ->

                            if (value != null) {

                                val documentList = value.toObjects(UserEntity::class.java)

                                val userList = mutableListOf<UserEntity>()

                                CoroutineScope(Dispatchers.IO).launch {

                                    documentList.forEach { user->

                                        val result = fireStore.collection("database/user/userList")
                                            .document(user.uid)
                                            .get()
                                            .await()

                                        if (result.exists()) {

                                            val userEntity = result.toObject(UserEntity::class.java)!!
                                            userEntity.loungeStatus = 3

                                            userList.add(userEntity)

                                        } else {
                                            Log.d("matched-null", "null")
                                        }

                                    }

                                    trySend(Response.Success(userList))

                                }                            }

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
                .set(mapOf("uid" to otherUser.uid))
                .await()

            fireStore.collection("database/user/userList/"+otherUser.uid+"/lounge/loungeInformation/matchingUserList")
                .document(ownUser.uid)
                .set(mapOf("uid" to ownUser.uid))
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

    override suspend fun saveFCMToken(): Response<Int> {

        try {

            val token = firebaseMessaging.token.await().toString()


            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge")
                .document("loungeInformation")
                .set(mapOf("fcm" to token))
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun getFCMToken(): Response<String> {

        try {

            val token = firebaseMessaging.token.await().toString()


            return Response.Success(token)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun postFCMToken(token: String): Response<Int> {

        try {

            fireStore.collection("database/user/userList/"+AppContext.uid+"/lounge/loungeInformation")
                .document()
                .set(token)
                .await()

            return Response.Success(SUCCESS)
        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override fun logOut(): Response<Int> {

        try {

            auth.signOut()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun deleteAccount(): Response<Int> {

        try {

            fireStore.collection("database/user/userList/")
                .document(AppContext.uid)
                .delete()
                .await()

            auth.signOut()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun inactivateAccount(): Response<Int> {

        try {

            fireStore.collection("database/user/userList/")
                .document(AppContext.uid)
                .update("deletedAt", Timestamp.now())
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun activateAccount(): Response<Int> {

        try {

            fireStore.collection("database/user/userList/")
                .document(AppContext.uid)
                .update("deletedAt", null)
                .await()

            return Response.Success(SUCCESS)
        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    private fun calAgeRange (birthYear : String) : Int {
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear.toInt()
    }


}