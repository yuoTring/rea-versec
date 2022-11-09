package com.versec.versecko.data.datasource.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.PublicStory
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.tasks.await

class StoryDataSourceImpl (

    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage,
    private val auth : FirebaseAuth

    ) : StoryDataSource {

    companion object {

        const val SUCCESS = 1
        const val ONE_DEPTH = 1
        const val TWO_DEPTH = 2
        const val NULL = "null"
        const val UID = "uid"
    }


    override suspend fun uploadStory(story: StoryEntity): Response<Int> {

        try {


            val path =
                fireStore.collection("database/story/storyList/").document().path

            story.uid = path.substringAfter("storyList/")


            fireStore.collection("database/story/storyList/")
                .document(story.uid)
                .set(story)
                .await()

            fireStore.collection("database/user/userList/"+auth.uid+"/story/")
                .document(story.uid)
                .set(

                    PublicStory (

                        uid = story.uid,
                        timestamp = story.timestamp

                            )
                )
                .await()


            val uploadMap = mutableMapOf<String, Uri>()

            story.uriMap.forEach { entry ->

                if (!entry.value.equals(NULL))
                uploadMap.put(entry.key, Uri.parse(entry.value))

            }

            val uploadImages = uploadImage(uploadMap, story.uid)

            when(uploadImages) {

                is Response.Success -> {
                    return Response.Success(SUCCESS)
                }
                is Response.Error -> {
                    return Response.Error(uploadImages.errorMessage+"___storage")
                } else -> {
                    return Response.Success(SUCCESS)
                }


            }

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString()+"___other")
        }
    }

    override suspend fun deleteStory(uid: String, size: Int): Response<Int> {

        try {

            fireStore.collection("database/story/storyList/")
                .document(uid)
                .delete()
                .await()

            fireStore.collection("database/user/userList/"+auth.uid+"/story")
                .document(uid)
                .delete()
                .await()


            for (index in 0..size - 1) {

                storage.reference.child("image/storyImages/"+auth.uid+"/"+uid+"/")
                    .child(uid+"__"+index)
                    .delete()
                    .await()

            }


            return Response.Success(SUCCESS)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }
    }


    override suspend fun getStoryList(

        startTimestamp: Long,
        location: String?,
        depth: Int

    ): Response<MutableList<StoryEntity>> {


        try {

            val collectionRef =
                fireStore.collection("database/story/storyList/")

            var querySnapshot : QuerySnapshot

            val stories = mutableListOf<StoryEntity>()

            if (location == null) {

                querySnapshot =

                    collectionRef
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .startAfter(startTimestamp)
                        .limit(1)
                        .get()
                        .await()


            } else {

                if (depth == ONE_DEPTH) {

                    querySnapshot =

                        collectionRef
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .startAfter(startTimestamp)
                            .whereEqualTo("mainLocation", location)
                            .limit(1)
                            .get()
                            .await()

                } else if (depth == TWO_DEPTH) {


                    querySnapshot =

                        collectionRef
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .startAfter(startTimestamp)
                            .whereEqualTo("subLocation", location)
                            .limit(1)
                            .get()
                            .await()

                } else {

                    querySnapshot =

                        collectionRef
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .startAfter(startTimestamp)
                            .limit(1)
                            .get()
                            .await()

                }

            }

            querySnapshot.documents.forEach { documentSnapshot ->

                documentSnapshot.toObject(StoryEntity::class.java)?.let { stories.add(it) }

            }

            return Response.Success(stories)

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }




    }

    override suspend fun getOwnStory(uid: String): Response<StoryEntity> {

        try {

            val dataSnapshot =
                fireStore.collection("database/story/storyList/")
                    .document(uid)
                    .get()
                    .await()

            val story =
                dataSnapshot.toObject<StoryEntity>()

            return Response.Success(story!!)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun getOwnStoryList(): Response<MutableList<StoryEntity>> {

        try {

            val collectionRef =
                fireStore.collection("database/user/userList/"+auth.uid+"/story").orderBy("timestamp", Query.Direction.DESCENDING)

            val querySnapshot =
                collectionRef.get().await()

            val storyUids = mutableListOf<String>()
            val stories = mutableListOf<StoryEntity>()

            querySnapshot.documents.forEach { documentSnapshot ->

                documentSnapshot.toObject(PublicStory::class.java)?.let { storyUids.add(it.uid) }

            }

            storyUids.forEach { uid ->


                val storySnapshot =
                    fireStore.collection("database/story/storyList/").whereEqualTo(UID, uid).get().await()

                storySnapshot.documents.forEach { documentSnapshot ->

                    documentSnapshot.toObject(StoryEntity::class.java)?.let { stories.add(it) }

                }

            }



            if (stories.size == 0)
                return Response.No(mutableListOf())
            else
                return Response.Success(stories)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>, uid: String): Response<Int> {

        try {

            val ownUid = auth.uid



            val uploadMap = mutableMapOf<String, String>()

            uriMap.forEach { entry ->

                val ref =
                    storage.reference.child("image/storyImages/"+ownUid+"/"+uid+"/"+uid+"__"+entry.key)

                ref.putFile(entry.value).await()
            }

            uriMap.forEach { entry ->

                val url =
                    storage.reference.child("image/storyImages/"+ownUid+"/"+uid+"/"+uid+"__"+entry.key).downloadUrl.await()


                uploadMap.put(entry.key, url.toString())

            }

            fireStore.collection("database/story/storyList/")
                .document(uid)
                .update("uriMap", uploadMap)
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun likeStory(uid: String): Response<StoryEntity> {


        try {

            fireStore.collection("database/story/storyList/")
                .document(uid)
                .update("likes", FieldValue.arrayUnion(auth.uid))
                .await()

            val documentSnapshot =
                fireStore.collection("database/story/storyList/")
                    .document(uid)
                    .get()
                    .await()

            val story = documentSnapshot.toObject<StoryEntity>()

            if (story != null)
                return Response.Success(story)
            else
                return Response.No(StoryEntity())

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }



    }

    override suspend fun cancelLike(uid: String): Response<StoryEntity> {

        try {

            fireStore.collection("database/story/storyList/")
                .document(uid)
                .update("likes", FieldValue.arrayRemove(auth.uid))
                .await()

            val documentSnapshot =
                fireStore.collection("database/story/storyList/")
                    .document(uid)
                    .get()
                    .await()

            val story = documentSnapshot.toObject<StoryEntity>()

            if (story != null)
                return Response.Success(story)
            else
                return Response.No(StoryEntity())

        } catch (exception : Exception) {

            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun reportStory(uid: String): Response<Int> {

        try {

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString()
            )
        }
    }
}