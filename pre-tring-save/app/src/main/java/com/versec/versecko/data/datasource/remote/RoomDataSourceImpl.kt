package com.versec.versecko.data.datasource.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RoomDataSourceImpl (

        private val databaseReference: DatabaseReference,
        private val auth : FirebaseAuth

        ) : RoomDataSource {

        companion object {
                val SUCCESS = 1
                val NULL = "null"
                val EMPTY = " "

                const val ADDED = 1
                const val REMOVED = 0
                const val CHANGED = 2
                const val ERROR = -1
                const val RESPONSE_NULL = -2
                const val LONG_NULL : Long = 0

        }

        override suspend fun insertUser(member: RoomMemberEntity): Response<Int> {


                try {

                        databaseReference.child("users").child(member.uid).setValue(member).await()

                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {

                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun getUser(uid: String): Response<RoomMemberEntity> {

                try {

                        val dataSnapshot =
                                databaseReference.child("users").child(uid).get().await()

                        val user = dataSnapshot.getValue(RoomMemberEntity::class.java)





                        if (user != null)
                                return Response.Success(user)
                        else
                                return Response.No(RoomMemberEntity())


                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }


        override suspend fun openRoom(

                otherUser: UserEntity,
                ownUser: UserEntity

        ): Response<Int> {

                try {

                        val roomUid =
                                databaseReference.child("rooms").push().key


                        val room = RoomEntity(
                                roomUid.toString(),
                                mutableListOf(AppContext.uid, otherUser.uid),
                                EMPTY
                        )




                        databaseReference.child("rooms").child(roomUid.toString()).setValue(room).await()

                        databaseReference.child("users").child(AppContext.uid).child("rooms").child(roomUid.toString()).setValue(RoomInUser(
                                roomUid.toString(),
                                System.currentTimeMillis()
                        )).await()

                        databaseReference.child("users").child(otherUser.uid).child("rooms").child(roomUid.toString()).setValue(RoomInUser(
                                roomUid.toString(),
                                System.currentTimeMillis()
                        )).await()




                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun getRoomForOneShot(roomUid: String): Response<RoomEntity?> {

                try {

                        val dataSnapshot =
                                databaseReference.child("rooms").child(roomUid).get().await()

                        val roomEntity =
                                dataSnapshot.getValue(RoomEntity::class.java)


                        return Response.Success(roomEntity)

                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun getAllRoomUidForOneShot(): Response<MutableList<RoomInUser>> {

                try {

                        val dataSnapshot = databaseReference.child("users").child(AppContext.uid).child("rooms").get().await()

                        val uids = mutableListOf<RoomInUser>()

                        dataSnapshot.children.forEach {

                                Log.d("uids-check", it.toString())
                        }

                        return Response.Success(uids)

                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun getLastMessage(roomUid: String): Response<MessageEntity> {

                try {

                        val dataSnapshot =
                                databaseReference.child("messages").child(roomUid).get().await()

                        val lastMessage =
                                dataSnapshot.children.last()

                        val message = lastMessage.getValue(MessageEntity::class.java)

                        Log.d("last-message-check", message.toString())


                        if (message != null)
                                return Response.Success(message)
                        else
                                return Response.No(MessageEntity())
                }
                catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }

        override fun observeRoomUid(): Flow<Map<Int, Response<RoomInUser>>> = callbackFlow {

                try {

                        val reference =
                                databaseReference.child("users").child(AppContext.uid).child("rooms")

                        val subscription =
                                reference.addChildEventListener(object : ChildEventListener {

                                        override fun onChildAdded(
                                                snapshot: DataSnapshot,
                                                previousChildName: String?
                                        ) {
                                                /**
                                                snapshot.getValue(RoomEntity::class.java)?.let { trySend(
                                                mapOf(1 to Response.Success(it))) } **/

                                                val roomInUser =
                                                        snapshot.getValue(RoomInUser::class.java)

                                                if (roomInUser != null)
                                                        trySend(mapOf(ADDED to Response.Success(roomInUser)))
                                                else
                                                        trySend(mapOf(RESPONSE_NULL to Response.No(RoomInUser())))

                                        }

                                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                                        }

                                        override fun onChildRemoved(snapshot: DataSnapshot) {

                                        }

                                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                                        override fun onCancelled(error: DatabaseError) {}


                                } )


                        awaitClose {

                                reference.removeEventListener(subscription)
                                channel.close()
                        }

                } catch (exception : Exception) {
                        trySend(mapOf(ERROR to Response.Error(exception.message.toString())))
                }
        }


        override fun observeLastMessage(roomUid: String): Flow<Response<String>> = callbackFlow {

                try {

                        val reference = databaseReference.child("rooms").child(roomUid)

                        val subscription = reference.addChildEventListener(object : ChildEventListener {

                                override fun onChildAdded(

                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {


                                }

                                override fun onChildChanged(


                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {

                                        val lastMessage = snapshot.getValue(String::class.java)

                                        if (lastMessage != null)
                                                trySend(Response.Success(lastMessage))
                                        else
                                                trySend(Response.No(NULL))
                                }

                                override fun onChildRemoved(snapshot: DataSnapshot) {

                                }

                                override fun onChildMoved(

                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                        } )


                        awaitClose {

                                reference.removeEventListener(subscription)
                                channel.close()
                        }

                } catch (exception : Exception) {
                        trySend(Response.Error(exception.message.toString()))
                }
        }

        override suspend fun getOwnLastRead(roomUid: String): Response<Long> {

                try {

                        val dataSnapshot =

                                databaseReference.child("users").child(AppContext.uid).child("rooms")
                                        .child(roomUid).get().await()


                        val roomInUser =
                                dataSnapshot.getValue(RoomInUser::class.java)

                        if (roomInUser != null)
                                return Response.Success(roomInUser.lastRead)
                        else
                                return Response.No(LONG_NULL)




                } catch (exception : Exception) {

                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun updateLastRead(roomUid: String): Response<Int> {

                try {

                        databaseReference.child("users").child(AppContext.uid)
                                .child("rooms").child(roomUid).child("lastRead").setValue(System.currentTimeMillis()).await()

                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {

                        return Response.Error(exception.message.toString())
                }
        }

        override fun fetchMessage(roomUid: String): Flow<Response<Map<Int, MessageEntity>>> = callbackFlow {


                try {

                        val reference =
                                databaseReference.child("messages").child(roomUid)

                        val subscription =
                                reference.addChildEventListener(object : ChildEventListener {


                                        override fun onChildAdded(

                                                snapshot: DataSnapshot,
                                                previousChildName: String?

                                        ) {

                                                val message = snapshot.getValue(MessageEntity::class.java)

                                                if (message != null)
                                                        trySend(Response.Success(mapOf(ADDED to message )))
                                                else
                                                        trySend(Response.Success(mapOf(RESPONSE_NULL to MessageEntity())))


                                        }

                                        override fun onChildChanged(

                                                snapshot: DataSnapshot,
                                                previousChildName: String?

                                        ) {

                                                val message = snapshot.getValue(MessageEntity::class.java)

                                                if (message != null)
                                                        trySend(Response.Success(mapOf(CHANGED to message)))
                                                else
                                                        trySend(Response.Success(mapOf(RESPONSE_NULL to MessageEntity())))
                                        }

                                        override fun onChildRemoved(snapshot: DataSnapshot) {

                                        }

                                        override fun onChildMoved(

                                                snapshot: DataSnapshot,
                                                previousChildName: String?

                                        ) {

                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }


                                } )


                        awaitClose {

                                reference.removeEventListener(subscription)
                                channel.close()
                        }





                } catch (exception : Exception) {

                        trySend(Response.Error(exception.message.toString()))
                }
        }

        override suspend fun sendMessage(contents: String, roomUid: String): Response<Int> {

                try {


                        val messageUid =
                                databaseReference.child("messages").child(roomUid).push().key.toString()

                        val ownUid = auth.currentUser?.uid.toString()

                        val message = MessageEntity (
                                messageUid,
                                contents,
                                ownUid,
                                System.currentTimeMillis(),
                                false
                        )


                        databaseReference.child("messages").child(roomUid).child(messageUid).setValue(message).await()
                        databaseReference.child("rooms").child(roomUid).child("lastSent").setValue(contents).await()

                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {

                        return Response.Error(exception.message.toString())
                }
        }

        override suspend fun readMessage(roomUid: String, messageUid: String): Response<Int> {

                try {

                        databaseReference
                                .child("messages").child(roomUid).child(messageUid).child("read").setValue(true).await()

                        databaseReference
                                .child("users").child(auth.currentUser!!.uid).child("rooms").child(roomUid).child("lastRead").setValue(System.currentTimeMillis())

                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {

                        return Response.Error(exception.message.toString())
                }

        }


        override fun observeOtherLastRead(roomUid: String, otherUid: String): Flow<Response<Long>> = callbackFlow {

                try {

                        val reference = databaseReference.child("users").child(otherUid).child(roomUid)

                        val subscription = reference.addChildEventListener(object : ChildEventListener {

                                override fun onChildAdded(

                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {

                                }

                                override fun onChildChanged(

                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {

                                        val lastRead = snapshot.getValue(Long::class.java)

                                        if (lastRead != null) {
                                                trySend(Response.Success(lastRead))
                                        } else {
                                                trySend(Response.Success(LONG_NULL))
                                        }

                                }

                                override fun onChildRemoved(snapshot: DataSnapshot) {
                                }

                                override fun onChildMoved(

                                        snapshot: DataSnapshot,
                                        previousChildName: String?

                                ) {

                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                        } )

                        awaitClose {

                                reference.removeEventListener(subscription)
                                channel.close()

                        }

                } catch (exception : Exception) {
                        trySend(Response.Error(exception.message.toString()))
                }
        }

        override suspend fun saveFCMToken(token: String): Response<Int> {

                try {

                        databaseReference
                                .child("users")
                                .child(AppContext.uid)
                                .child("fcm")
                                .setValue(token)
                                .await()

                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }


}