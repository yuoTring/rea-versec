package com.versec.versecko.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.ChatRoomMemberEntity
import com.versec.versecko.databinding.ItemRecyclerChatroomBinding

class ChatRoomAdapter (

    private var chatRoomList : MutableList<ChatRoomEntity>,
    private val onRoomClick : (ChatRoomEntity?) -> Unit

        ) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {


    inner class ViewHolder (val binding : ItemRecyclerChatroomBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (room : ChatRoomEntity, onRoomClick: (ChatRoomEntity?) -> Unit) {

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
        lateinit var member : ChatRoomMemberEntity

        holder.bind(room, onRoomClick)

        room.memberMap.values.forEach {

            if (it.uid != AppContext.uid)
                member = it
        }

        holder.binding.textNickName.setText(member.nickName)
        holder.binding.textMessageSumnail.setText(room.lastMessageSent)

        Glide
            .with(holder.binding.root)
            .load(member.uri)
            .into(holder.binding.imageProfile)



    }

    override fun getItemCount(): Int { return chatRoomList.size }
    fun changeRooms (chatRoomList: MutableList<ChatRoomEntity>) { this.chatRoomList = chatRoomList }
}