package com.versec.versecko.view.profile.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.databinding.ItemViewpagerBinding

class ViewPagerAdapter (

    private var uriList : MutableList<String>


        ) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide
            .with(holder.binding.root)
            .load(Uri.parse(uriList.get(position)))
            .into(holder.binding.imageProfileImage)
    }

    override fun getItemCount(): Int {
        return uriList.size
    }

    fun updateImages(list: MutableList<String>) {
        this.uriList = list
    }
}