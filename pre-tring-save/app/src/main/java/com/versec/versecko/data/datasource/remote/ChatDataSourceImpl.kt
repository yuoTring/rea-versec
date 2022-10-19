package com.versec.versecko.data.datasource.remote

import android.os.Build
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.*
import com.versec.versecko.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class ChatDataSourceImpl (

        private val databaseReference: DatabaseReference,
        private val auth : FirebaseAuth

        ) : ChatDataSource {

        companion object {
                val SUCCESS = 1
                val NULL = "null"

                const val ADDED = 1
                const val REMOVED = 0
                const val CHANGED = 2
                const val ERROR = -1
                const val R_NULL = -2

                val CALENDAR = Calendar.getInstance()
                val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
                val TIME_FORMAT = SimpleDateFormat("HH:mm")

        }

        override suspend fun insertUser(member: RoomMemberEntity): Response<Int> {


                try {

                        databaseReference.child("users").child(member.uid).setValue(member).await()

                        return Response.Success(SUCCESS)

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
                                NULL
                        )




                        databaseReference.child("rooms").child(roomUid.toString()).setValue(room).await()

                        databaseReference.child("users").child(AppContext.uid).child("rooms").child(roomUid.toString()).setValue(Room(
                                roomUid.toString(),
                                System.currentTimeMillis()
                        )).await()

                        databaseReference.child("users").child(otherUser.uid).child("rooms").child(roomUid.toString()).setValue(Room(
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

        override fun observeRoomUid(): Flow<Map<Int, Response<Room>>> = callbackFlow {

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

                                                val room =
                                                        snapshot.getValue(Room::class.java)

                                                Log.d("room-check", room.toString())

                                                if (room != null)
                                                        trySend(mapOf(ADDED to Response.Success(room)))
                                                else
                                                        trySend(mapOf(R_NULL to Response.Success(Room())))

                                        }

                                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                                        }

                                        override fun onChildRemoved(snapshot: DataSnapshot) {

                                        }

                                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                                        override fun onCancelled(error: DatabaseError) {}


                                } )


                        awaitClose { reference.removeEventListener(subscription) }

                } catch (exception : Exception) {
                        trySend(mapOf(ERROR to Response.Error(exception.message.toString())))
                }
        }

        override fun observeRoom(roomUid: String): Flow<Response<RoomEntity>> = callbackFlow {


                val reference =
                        databaseReference.child("rooms").child(roomUid)

                val subscription =
                        reference.addChildEventListener(object : ChildEventListener {

                                override fun onChildAdded(
                                        snapshot: DataSnapshot,
                                        previousChildName: String?
                                ) {



                                        val roomEntity =
                                                snapshot.getValue(RoomEntity::class.java)


                                        if (roomEntity != null)
                                                trySend(Response.Success(roomEntity))
                                }

                                override fun onChildChanged(
                                        snapshot: DataSnapshot,
                                        previousChildName: String?
                                ) {

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

                awaitClose { reference.removeEventListener(subscription) }


        }


        override fun deleteRoom(chatRoomUid: String, otherUid: String) {

                //databaseReference.child("chat").child("chatRoomList").child(chatRoomUid).removeValue()

        }

        override suspend fun sendMessage(content: String, room: RoomEntity): Response<Int> {

                try {

                        val messageUid = databaseReference.child("chat").child("messageList").child(room.uid).push().key


                        lateinit var sender : RoomMemberEntity



                        val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {

                                /**
                                MessageEntity(
                                        messageUid!!,
                                        content,
                                        room.chatRoomUid,
                                        sender,
                                        receiver,
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        false
                                )**/
                        }
                        else
                        {
                                val calendar = Calendar.getInstance()
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                val timeFormat = SimpleDateFormat("HH:mm")

                                /**
                                MessageEntity(
                                        messageUid!!,
                                        content,
                                        room.chatRoomUid,
                                        sender,
                                        receiver,
                                        dateFormat.format(calendar.time),
                                        timeFormat.format(calendar.time),
                                        false
                                )**/

                        }






                        // must edit in later to handle an error
                        return Response.Success(SUCCESS)


                } catch (exception : Exception) {


                        return Response.Error(exception.message.toString())
                }

        }


        override fun getMessages(chatRoomUid: String): Flow<MessageEntity> = callbackFlow {

                val ref = databaseReference.child("chat").child("messageList").child(chatRoomUid)

                val listener = ref.addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {

                                snapshot.getValue(MessageEntity::class.java)?.let { trySend(it) }
                        }

                        override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {
                                snapshot.getValue(MessageEntity::class.java)?.let { trySend(it) }
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




                        })

                awaitClose { ref.removeEventListener(listener)  }
        }

        override suspend fun resetUnreadMessageCounter(chatRoomUid: String): Response<Int> {

                try {
                        databaseReference.child("chat").child("chatRoomListByUser").child(AppContext.uid)
                                .child(chatRoomUid).child("unreadMessageCounter"
                                ).setValue(0).await()

                        return Response.Success(SUCCESS)

                }
                catch (exception : Exception) {
                   return Response.Error(exception.message.toString())
                }
        }

        override suspend fun resetReadOrNot(message: MessageEntity): Response<Int> {

                try {


                        return Response.Success(SUCCESS)

                }
                catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
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