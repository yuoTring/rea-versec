package com.versec.versecko.view.matching.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ItemCardstackUserBinding
import com.versec.versecko.databinding.ItemCardstackViewpagerBinding
import com.versec.versecko.databinding.ItemViewpagerBinding
import com.versec.versecko.view.matching.UserProfileActivity

class CardStackAdapter (

    private var userList: List<UserEntity>,
    private val onImageClick: (String?) -> Unit

) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {


    inner class ViewHolder (val binding: ItemCardstackUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind (text : String?, onImageClick: (String?) -> Unit ) {

                onImageClick(text)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemCardstackUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var user = userList.get(position)


        holder.binding.textNickAndAge.setText(user.nickName+", "+user.age)
        holder.binding.textResidence.setText(user.mainResidence)
        holder.binding.textMannerScore.setText("매너 점수: "+ user.mannerScore)



        var tempUriList : MutableList<String> = mutableListOf()

        user.uriMap.forEach { entry ->
            tempUriList.add(entry.key.toInt(), entry.value)
        }

        tempUriList.removeAll(listOf("null"))

        val cardViewPagerAdapter = CardViewPagerAdapter(tempUriList) {

            var viewpager = holder.binding.viewpager

            if (it.equals("left")) {

                if (viewpager.currentItem>0)
                viewpager.setCurrentItem(--viewpager.currentItem)

            }
            else if (it.equals("right")) {


                if (viewpager.currentItem<user.uriMap.size-1)
                viewpager.setCurrentItem(++viewpager.currentItem)

            }
            else {
                    onImageClick(it)
            }

        }

        holder.binding.viewpager.adapter = cardViewPagerAdapter

        TabLayoutMediator(holder.binding.tabLayout, holder.binding.viewpager) {
                tab , position ->

        }.attach()



    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList (userList: List<UserEntity>) {
        this.userList = userList
    }
















    inner class CardViewPagerAdapter (

        private val uriList : MutableList<String>,
        private val onImageClick : (String?) -> Unit

    ) : RecyclerView.Adapter<CardViewPagerAdapter.ViewPagerViewHolder>() {


        inner class ViewPagerViewHolder (val binding : ItemCardstackViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind (onImageClick: (String?) -> Unit) {

                binding.toLeft.setOnClickListener {

                    onImageClick("left")
                }

                binding.toDetailProfile.setOnClickListener {
                    onImageClick("detail")
                }

                binding.toRight.setOnClickListener {

                    onImageClick("right")

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

            val binding = ItemCardstackViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewPagerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

            holder.bind(onImageClick)

            val corner = 16


            if (!uriList.get(position).equals("null")) {

                Log.d("uri-check", uriList.get(position))
                Glide
                    .with(holder.binding.root)
                    .load(Uri.parse(uriList.get(position)))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(corner)))
                    .into(holder.binding.imageProfileImage)
            }

        }

        override fun getItemCount(): Int {

            return uriList.size
        }


    }
}