package com.versec.versecko.view.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.RoomMemberEntity
import com.versec.versecko.databinding.ItemRecyclerRoomBinding

class RoomAdapter (

    private var roomList : MutableList<RoomEntity>,
    private var otherUserList : MutableList<RoomMemberEntity>,
    private var notificationMap : MutableMap<String, Boolean>,

    private val onRoomClick : (RoomEntity?) -> Unit

        ) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {


    inner class ViewHolder (val binding : ItemRecyclerRoomBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (room : RoomEntity, onRoomClick: (RoomEntity?) -> Unit) {

            binding.root.setOnClickListener {
                onRoomClick(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(roomList.get(position), onRoomClick)

        holder.binding.textNickName.setText(otherUserList.get(position).nickName)

        Glide
            .with(holder.binding.root)
            .load(otherUserList.get(position).profileUrl)
            .into(holder.binding.imageProfile)

        holder.binding.textMessageSumnail.setText(roomList.get(position).lastSent)


        if (notificationMap.size > position) {

            if (notificationMap.get(roomList.get(position).uid) == true)
                holder.binding.textNewMessageNotification.visibility = View.VISIBLE
            else
                holder.binding.textNewMessageNotification.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int { return roomList.size }

    fun changeRooms (chatRoomList: MutableList<RoomEntity>) { this.roomList = chatRoomList }

    fun changeOtherUsers (otherUserList: MutableList<RoomMemberEntity>) {
        this.otherUserList = otherUserList
    }

    fun changeNotificationMap (notificationMap : MutableMap<String, Boolean>) {
        this.notificationMap = notificationMap
    }

    fun updateUserInfo (roomMember : RoomMemberEntity) {

        otherUserList.forEachIndexed { index, otherUser ->

            if (otherUser.uid.equals(roomMember.uid))
                otherUserList.set(index,roomMember
                )

        }
    }
}