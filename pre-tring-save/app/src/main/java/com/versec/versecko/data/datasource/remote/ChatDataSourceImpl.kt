package com.versec.versecko.data.datasource.remote

import android.os.Build
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.ChatRoomMemberEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatDataSourceImpl (

        private val databaseReference: DatabaseReference,
        private val auth : FirebaseAuth

        ) : ChatDataSource {

        companion object {
                val SUCCESS = 1
        }



        override suspend fun openChatRoom(
                otherUser: UserEntity,
                ownUser: UserEntity
        ): Response<Int> {

                try {

                        val roomId =
                                databaseReference.child("chat").child("ChatRoomListByUser").child(ownUser.uid).push().key

                        val room = ChatRoomEntity(

                                roomId!!,
                                mutableMapOf(
                                        otherUser.uid to ChatRoomMemberEntity(otherUser.uid, otherUser.nickName, otherUser.uriMap.get("0")!!),
                                        ownUser.uid to ChatRoomMemberEntity(ownUser.uid, ownUser.nickName, ownUser.uriMap.get("0")!!)),
                                0,
                                "no message sent yet"
                        )

                        databaseReference.child("chat").child("chatRoomListByUser").child(ownUser.uid).child(roomId).setValue(room).await()
                        databaseReference.child("chat").child("chatRoomListByUser").child(otherUser.uid).child(roomId).setValue(room).await()


                        return Response.Success(SUCCESS)

                } catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }

        override fun observeChatRoom(): Flow<Map<Int, Response<ChatRoomEntity>>> = callbackFlow {

                try {

                        val uid = AppContext.uid

                        val reference = databaseReference.child("chat").child("chatRoomListByUser").child(uid)

                        val subscription =
                                reference.addChildEventListener(object :ChildEventListener {

                                        override fun onChildAdded(
                                                snapshot: DataSnapshot,
                                                previousChildName: String?
                                        ) {
                                                snapshot.getValue(ChatRoomEntity::class.java)?.let { trySend(
                                                        mapOf(1 to Response.Success(it))) }
                                        }

                                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                                                snapshot.getValue(ChatRoomEntity::class.java)?.let { trySend(
                                                        mapOf(2 to Response.Success(it))
                                                ) }
                                        }

                                        override fun onChildRemoved(snapshot: DataSnapshot) {

                                                snapshot.getValue(ChatRoomEntity::class.java)?.let { trySend(
                                                        mapOf(0 to Response.Success(it))) }

                                        }

                                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                                        override fun onCancelled(error: DatabaseError) {}


                                } )


                        awaitClose { reference.removeEventListener(subscription) }

                } catch (exception : Exception) {
                        trySend(mapOf(-1 to Response.Error(exception.message.toString())))
                }
        }


        override fun deleteChatRoom(chatRoomUid: String, otherUid: String) {

                databaseReference.child("chat").child("chatRoomList").child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("chatRoomByUser").child(auth.currentUser!!.uid).child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("chatRoomByUser").child(otherUid).child(chatRoomUid).removeValue()
                databaseReference.child("chat").child("messageList").child(chatRoomUid).removeValue()
        }

        override suspend fun sendMessage(content: String, room: ChatRoomEntity): Response<Int> {

                try {

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
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        false
                                )
                        }
                        else
                        {
                                val calendar = Calendar.getInstance()
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                val timeFormat = SimpleDateFormat("HH:mm")

                                MessageEntity(
                                        messageUid!!,
                                        content,
                                        room.chatRoomUid,
                                        sender,
                                        receiver,
                                        dateFormat.format(calendar.time),
                                        timeFormat.format(calendar.time),
                                        false
                                )

                        }



                        databaseReference.child("chat").child("messageList").child(room.chatRoomUid).child(messageUid).setValue(message)

                        databaseReference.child("chat").child("chatRoomListByUser").child(receiver.uid).child(room.chatRoomUid).child("unreadMessageCounter").setValue(ServerValue.increment(1.0)).await()

                        databaseReference.child("chat").child("chatRoomListByUser").child(sender.uid).child(room.chatRoomUid).child("lastMessageSent").setValue(message.contents).await()

                        databaseReference.child("chat").child("chatRoomListByUser").child(receiver.uid).child(room.chatRoomUid).child("lastMessageSent").setValue(message.contents).await()


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
                                .child(chatRoomUid).child("unreadMessageCounter").setValue(0).await()

                        return Response.Success(SUCCESS)

                }
                catch (exception : Exception) {
                   return Response.Error(exception.message.toString())
                }
        }

        override suspend fun resetReadOrNot(message: MessageEntity): Response<Int> {

                try {
                        databaseReference.child("chat").child("messageList").child(message.chatRoomUid)
                                .child(message.messageUid).child("readed").setValue(true)

                        return Response.Success(SUCCESS)

                }
                catch (exception : Exception) {
                        return Response.Error(exception.message.toString())
                }
        }


}