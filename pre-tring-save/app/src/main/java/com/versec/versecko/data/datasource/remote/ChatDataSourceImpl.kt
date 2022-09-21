package com.versec.versecko.data.datasource.remote

import android.os.Build
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.ChatRoomMemberEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Results
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatDataSourceImpl (

        private val databaseReference: DatabaseReference,
        private val auth : FirebaseAuth

        ) : ChatDataSource {


        override fun openChatRoom(otherUser: UserEntity, ownUser: UserEntity) {

                val chatRoomUid = databaseReference.child("chat").child("chatRoomByUser").child(ownUser.uid).push().key


                var chatRoom = ChatRoomEntity(

                        chatRoomUid!!,
                        mutableMapOf(
                                otherUser.uid to ChatRoomMemberEntity(otherUser.uid, otherUser.nickName, otherUser.uriMap.get("0")!!),
                                ownUser.uid to ChatRoomMemberEntity(ownUser.uid, ownUser.nickName, ownUser.uriMap.get("0")!!)
                                ),
                        0,
                        "no message sent yet"
                )

                databaseReference
                        .child("chat").child("chatRoomListByUser").child(otherUser.uid).child(chatRoomUid).setValue(chatRoom)

                databaseReference
                        .child("chat").child("chatRoomListByUser").child(ownUser.uid).child(chatRoomUid).setValue(chatRoom)

                val messageUid = databaseReference.child("chat").child("messageList").child(chatRoomUid).push().key

                sendMessage("Hi, it is greeting message of this room !!!", chatRoom)


        }

        override suspend fun getAllChatRooms(): MutableList<ChatRoomEntity> {

                var uid = "test!!!!!"

                var chatRooms = mutableListOf<ChatRoomEntity>()

                databaseReference.child("chat").child("chatRoomListByUser").child(uid).get()
                        .addOnCompleteListener {

                                it.result.children.forEach {

                                        chatRooms.add(it.getValue(ChatRoomEntity::class.java)!!)

                                }

                        }.await()

                return chatRooms
        }

        override fun getUpdatedChatRoom(): Flow<ChatRoomEntity> = callbackFlow {

                var uid = "test!!!!!"


                val reference = databaseReference.child("chat").child("chatRoomListByUser").child(uid)

                Log.d("room-get", "getUpdatedChatRoom -> "+reference.toString())

                var subscription =
                reference.addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {
                                Log.d("room-get", "getUpdatedChatRoom -> added")

                                trySend(snapshot.getValue(ChatRoomEntity::class.java)!!)
                                Log.d("room-get", "getUpdatedChatRoom ->" + snapshot.getValue(ChatRoomEntity::class.java)!!.chatRoomUid.toString())

                        }

                        override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {

                                Log.d("room-get", "getUpdatedChatRoom -> changed")

                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                                Log.d("room-get", "getUpdatedChatRoom -> removed")
                        }

                        override fun onChildMoved(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {
                                Log.d("room-get", "getUpdatedChatRoom -> moved")
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }


                })

                awaitClose { reference.removeEventListener(subscription) }



        }

        override fun deleteChatRoom(chatRoomUid: String, otherUid: String) {

                databaseReference.child("chat").child("chatRoomList").child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("chatRoomByUser").child(auth.currentUser!!.uid).child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("chatRoomByUser").child(otherUid).child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("messageList").child(chatRoomUid).removeValue()
        }

        override fun sendMessage(content: String, room: ChatRoomEntity): Results<Int> {

                val messageUid = databaseReference.child("chat").child("messageList").child(room.chatRoomUid).push().key

                lateinit var sender : ChatRoomMemberEntity
                lateinit var receiver : ChatRoomMemberEntity

                room.memberMap.values.forEach {

                        if (it.uid.equals(AppContext.uid)) {
                                sender = it
                        } else {
                                receiver = it
                        }

                }

                val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {

                        MessageEntity(
                                messageUid!!,
                                content,
                                room.chatRoomUid,
                                sender,
                                receiver,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:ss")),
                                false
                        )
                }
                else
                {
                        TODO("VERSION.SDK_INT < O")
                }

                databaseReference.child("chat").child("messageList").child(room.chatRoomUid).child(messageUid).setValue(message)

                databaseReference.child("chat").child("chatRoomListByUser").child(receiver.uid).child(room.chatRoomUid).child("unreadMessageCounter").setValue(ServerValue.increment(1.0))

                databaseReference.child("chat").child("chatRoomListByUser").child(sender.uid).child(room.chatRoomUid).child("lastMessageSent").setValue(message.contents)

                databaseReference.child("chat").child("chatRoomListByUser").child(receiver.uid).child(room.chatRoomUid).child("lastMessageSent").setValue(message.contents)

                // must edit in later to handle an error
                return Results.Success(0)
        }


        override suspend fun getAllMessages(chatRoomUid: String): MutableList<MessageEntity> {

                var uid = "test!!!!!"

                var list = mutableListOf<MessageEntity>()

                databaseReference.child("chat").child("messageList").child(chatRoomUid).get()
                        .addOnCompleteListener {

                                it.result.children.forEach {

                                        list.add(it.getValue(MessageEntity::class.java)!!)
                                        Log.d("message-get", it.getValue(MessageEntity::class.java)!!.contents)
                                }

                        }.await()

                databaseReference.child("chat").child("chatRoomListByUser").child(uid).child(chatRoomUid).child("unreadMessageCounter").setValue(0).await()

                return list
        }

        override fun getUpdatedMessage(chatRoomUid: String): Flow<MessageEntity> = callbackFlow {


                val reference = databaseReference.child("chat").child("messageList").child(chatRoomUid)

                val subscription = reference.addChildEventListener(object : ChildEventListener{

                        override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                        ) {

                                Log.d("message-get", "getUpdatedMessage-added")
                                trySend(snapshot.getValue(MessageEntity::class.java)!!)

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

                })

                awaitClose { reference.removeEventListener(subscription) }


        }


}