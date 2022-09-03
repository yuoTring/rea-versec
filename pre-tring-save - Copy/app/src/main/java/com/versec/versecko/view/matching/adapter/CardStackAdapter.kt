package com.versec.versecko.view.matching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ItemCardstackUserBinding

class CardStackAdapter (

    private var userList: List<UserEntity>

        ) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

            inner class ViewHolder (val binding: ItemCardstackUserBinding) :
                RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemCardstackUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}