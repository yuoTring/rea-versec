package com.versec.versecko.view.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.RoomMemberEntity
import com.versec.versecko.databinding.ItemRecyclerChatroomBinding

class ChatRoomAdapter (

    private var chatRoomList : MutableList<RoomEntity>,
    private val onRoomClick : (RoomEntity?) -> Unit

        ) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {


    inner class ViewHolder (val binding : ItemRecyclerChatroomBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (room : RoomEntity, onRoomClick: (RoomEntity?) -> Unit) {

            binding.root.setOnClickListener {
                onRoomClick(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val room = chatRoomList.get(position)
        lateinit var member : RoomMemberEntity

        holder.bind(room, onRoomClick)



        holder.binding.textNickName.setText(member.nickName)
        holder.binding.textMessageSumnail.setText(room.lastSent)

        if (room) {

            holder.binding.textUnreadMessageCounter.visibility = View.VISIBLE
            holder.binding.textUnreadMessageCounter.setText()
        }
        else {
            holder.binding.textUnreadMessageCounter.visibility = View.INVISIBLE
        }

        Glide
            .with(holder.binding.root)
            .load(member.profileUrl)
            .into(holder.binding.imageProfile)



    }

    override fun getItemCount(): Int { return chatRoomList.size }
    fun changeRooms (chatRoomList: MutableList<RoomEntity>) { this.chatRoomList = chatRoomList }
}