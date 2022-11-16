package com.versec.versecko.view.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.data.entity.LoungeUser
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ItemRecyclerLoungeProfileBinding

class LoungeAdapter (

    private var userList: MutableList<LoungeUser>,
    private var timestamp : Long,
    private val onProfileClick : (LoungeUser?) -> Unit

        ) : RecyclerView.Adapter<LoungeAdapter.ViewHolder>() {

    inner class ViewHolder (val binding : ItemRecyclerLoungeProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (profile : LoungeUser, onProfileClick: (LoungeUser?) -> Unit) {

            binding.root.setOnClickListener {

                onProfileClick(profile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerLoungeProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList.get(position)

        if (timestamp < user.timestamp)
            holder.binding.imageOutLine.visibility = View.VISIBLE
        else
            holder.binding.imageOutLine.visibility = View.INVISIBLE

        holder.bind(user, onProfileClick)

        holder.binding.textNickName.setText(user.nickName)
        Glide
            .with(holder.binding.root)
            .load(user.uriMap.get("0"))
            .into(holder.binding.imageProfile)

    }

    override fun getItemCount(): Int { return userList.size }

    fun changeUsers (userList: MutableList<LoungeUser>) { this.userList = userList }

    fun updateTimestamp (timestamp: Long) { this.timestamp = timestamp }

}