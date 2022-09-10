package com.versec.versecko.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ItemRecyclerLoungeProfileBinding

class LoungeAdapter (

    private var userList : MutableList<UserEntity>

        ) : RecyclerView.Adapter<LoungeAdapter.ViewHolder>() {

    inner class ViewHolder (val binding : ItemRecyclerLoungeProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerLoungeProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var user = userList.get(position)

        holder.binding.textNickName.setText(user.nickName)
        Glide
            .with(holder.binding.root)
            .load(user.uriMap.get("0"))
            .into(holder.binding.imageProfile)


    }

    override fun getItemCount(): Int {

        return userList.size
    }
}