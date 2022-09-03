package com.versec.versecko.data.datasource.remote

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun insertImage(mutableList: MutableList<Bitmap>): Results<Int> {

        val uid = auth.currentUser!!.uid

        mutableList.forEachIndexed { index, bitmap ->


            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val reference =
                storage.reference.child("images/profileImages/"+ uid+"/"+index+"/")

            val uploadTask : UploadTask = reference.putBytes(data)




        }
        TODO("Not yet implemented")
    }

    override fun getImageUri(): Flow<String> {
        TODO("Not yet implemented")
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
    ): List<UserEntity>  {

        //val geoHash = GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude))

        val center = GeoLocation(latitude, longitude)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter.toDouble())

        val tasks : MutableList<Task<QuerySnapshot>> = mutableListOf()

        val boundSize : Int = bounds.size

        var docs = mutableListOf<DocumentSnapshot>()
        var userList = mutableListOf<UserEntity>()

        bounds.forEach { geoQueryBounds ->

            val query = fireStore.collection("database/user/userList")
                .orderBy("geohash")
                .startAt(geoQueryBounds.startHash)
                .endAt(geoQueryBounds.endHash)

            tasks.add(query.get())

        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {

                tasks.forEach { task ->

                    val snap = task.getResult()

                    snap.forEach { doc ->
                        docs.add(doc)
                    }
                }

            }.await()


        docs.forEach { document ->

            val user : UserEntity? = document.toObject(UserEntity::class.java)

            if (user != null) {
                userList.add(user)
            }
        }

        return userList

    }

    override suspend fun likeUser(userEntity: UserEntity) {

        fireStore.collection("database/user/userList/"+ auth.currentUser!!.uid+"/likeList")
            .document(userEntity.uid)
            .set(userEntity)

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

            val downloadRef =
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


}