package com.versec.versecko.view.matching.adapter

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

        var user = userList.get(position)


        holder.binding.textNickAndAge.setText(user.nickName+", "+user.age)
        holder.binding.textResidence.setText(user.mainResidence)
        holder.binding.textMannerScore.setText(user.mannerScore.toString())

        var tempUriList : MutableList<String> = mutableListOf()

        user.uriMap.forEach { entry ->
            tempUriList.add(entry.key.toInt(), entry.value)
        }

        //Log.d("uri", "size-> "+tempUriList.size)
        //Log.d("uri", "uri-list-> "+ tempUriList.toString())

        Log.d("uri-check", "size -> "+tempUriList.size)
        Log.d("uri-check", "size -> "+tempUriList.toString())


        val cardViewPagerAdapter = CardViewPagerAdapter(tempUriList) {

            Log.d("click-test", "!!!")
            Log.d("click-test", user.toString())

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

            fun bind (temp : String?, onImageClick: (String?) -> Unit) {

                binding.toLeft.setOnClickListener {

                    onImageClick(temp)
                }

                binding.toDetailProfile.setOnClickListener {
                    onImageClick(temp)
                }

                binding.toRight.setOnClickListener {

                    onImageClick(temp)

                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

            val binding = ItemCardstackViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewPagerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

            holder.bind(uriList.get(position), onImageClick)

            val corner = 16

            Glide
                .with(holder.binding.root)
                .load(Uri.parse(uriList.get(position)))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(corner)))
                .into(holder.binding.imageProfileImage)
        }

        override fun getItemCount(): Int {
            return userList.size
        }

    }
}