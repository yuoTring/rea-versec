package com.versec.versecko.data.datasource.remote

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.tasks.await

class StoryDataSourceImpl (

    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage,

    ) : StoryDataSource {

    companion object {

        const val SUCCESS = 1
        const val ONE_DEPTH = 1
        const val TWO_DEPTH = 2
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

            fireStore.collection("database/user/userList/"+AppContext.uid+"/story/storyList/")
                .document(story.uid)
                .set(story)
                .await()


            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
    }

    override suspend fun editStory(story: StoryEntity): Response<Int> {

        try {

            fireStore.collection("database/story/storyList/")
                .document(story.uid)
                .set(story)
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }

    }

    override suspend fun deleteStory(uid: String): Response<Int> {

        try {

            fireStore.collection("database/story/storyList/")
                .document(uid)
                .delete()
                .await()

            storage.reference.child("image/storyImages/"+AppContext.uid+"/")
                .child(uid)
                .delete()
                .await()

            return Response.Success(SUCCESS)

        } catch (exception : Exception) { return Response.Error(exception.message.toString()) }
    }

    override suspend fun getStoryList(

        startUid: String,
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
                        .orderBy("uid")
                        .startAfter(startUid)
                        .limit(10)
                        .get()
                        .await()



            } else {


                if (depth == ONE_DEPTH) {

                    querySnapshot =

                        collectionRef
                            .whereEqualTo("mainLocation", location)
                            .orderBy("uid")
                            .startAfter(startUid)
                            .limit(10)
                            .get()
                            .await()

                } else if (depth == TWO_DEPTH) {

                    querySnapshot =

                        collectionRef
                            .whereEqualTo("subLocation", location)
                            .orderBy("uid")
                            .startAfter(startUid)
                            .limit(10)
                            .get()
                            .await()

                } else {

                    querySnapshot =

                        collectionRef
                            .orderBy("uid")
                            .startAfter(startUid)
                            .limit(10)
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

    override suspend fun getOwnStoryList(): Response<MutableList<StoryEntity>> {

        try {

            val collectionRef =
                fireStore.collection("database/user/userList/"+AppContext.uid+"/story/storyList/")

            val querySnapshot =
                collectionRef.get().await()

            val stories = mutableListOf<StoryEntity>()

            querySnapshot.documents.forEach { documentSnapshot ->

                documentSnapshot.toObject(StoryEntity::class.java)?.let { stories.add(it) }

            }


            return Response.Success(stories)

        } catch (exception : Exception) {
            return Response.Error(exception.message.toString())
        }
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uriMap: MutableMap<String, Uri>, uid: String): Response<Int> {

        try {



            val uploadMap = mutableMapOf<String, String>()

            uriMap.forEach { entry ->

                val ref =
                    storage.reference.child("image/storyImages/"+AppContext.uid+"/"+uid+"/"+uid+"__"+entry.key)

                ref.putFile(entry.value).await()
            }

            uriMap.forEach { entry ->

                val url =
                    storage.reference.child("image/storyImages/"+AppContext.uid+"/"+uid+"/"+uid+"__"+entry.key).downloadUrl.await()


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
}