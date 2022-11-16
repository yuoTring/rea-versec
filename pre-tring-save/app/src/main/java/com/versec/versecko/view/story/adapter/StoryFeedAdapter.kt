package com.versec.versecko.view.story.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.databinding.InfiniteScrollLoadingBinding
import com.versec.versecko.databinding.ItemRecyclerStoryBinding
import com.versec.versecko.view.profile.adapter.ViewPagerAdapter
import java.text.SimpleDateFormat
import java.util.*

class StoryFeedAdapter (

    private val activity : Activity,
    private var stories : MutableList<StoryEntity?>,
    private val onClick : (Int, Int, View)-> Unit

        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val NULL = 0
        private const val EXIST = 1

        private const val TYPE_LOADING_SEEMORE = 40000

        private const val TYPE_EXIST_LIKE = 40020
        private const val TYPE_REPORT = 40030
    }


    inner class ViewHolderLoading (val binding : InfiniteScrollLoadingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (position: Int, onClick: (Int, Int, View) -> Unit) {

            binding.textSeeMore.setOnClickListener {

                onClick(position, TYPE_LOADING_SEEMORE, binding.textSeeMore)

            }

        }
    }

    inner class ViewHolder (val binding: ItemRecyclerStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (position: Int, onClick: (Int, Int, View) -> Unit) {


            binding.buttonLike.setOnClickListener {

                onClick(position, TYPE_EXIST_LIKE, binding.buttonLike)
            }

            binding.buttonMore.setOnClickListener {

                onClick(position, TYPE_REPORT, binding.buttonMore)
            }


        }
    }

    override fun getItemViewType(position: Int): Int {

        if (stories.get(position) == null)
            return NULL
        else
            return EXIST

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == NULL)

            return ViewHolderLoading(

                    InfiniteScrollLoadingBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
        else
            return ViewHolder(

                ItemRecyclerStoryBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
            )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val story = stories.get(position)


        if (holder.itemViewType == NULL) {

            val viewHolder = holder as ViewHolderLoading

            viewHolder.bind(position, onClick)



        } else {

            val viewHolder = holder as ViewHolder

            viewHolder.bind(position, onClick)


            if (story != null) {


                val uriList = mutableListOf<String>()

                story.uriMap.forEach { entry -> uriList.add(entry.key.toInt(), entry.value) }

                val adapter = ViewPagerAdapter(uriList)

                viewHolder.binding.viewpagerStoryImage.adapter = adapter

                TabLayoutMediator(viewHolder.binding.tabLayout, viewHolder.binding.viewpagerStoryImage) { tab, position -> }.attach()


                if (story.likes.contains(AppContext.uid)) {

                    viewHolder.binding.buttonLike.setBackgroundResource(activity.resources.getIdentifier("icon_heart_story", "drawable", activity.packageName))

                } else {

                    viewHolder.binding.buttonLike.setBackgroundResource(activity.resources.getIdentifier("icon_heart_empty_story", "drawable", activity.packageName))
                }


                viewHolder.binding.textNickName.setText(story.userNickName)
                viewHolder.binding.textStoryContents.setText(story.contents)
                viewHolder.binding.textStoryLocaton.setText(story.subLocation+", "+story.mainLocation)

                if (story.likes.size > 0) {

                    viewHolder.binding.textLikeCount.visibility = View.VISIBLE
                    viewHolder.binding.textLikeCount.setText(story.likes.size.toString())
                    viewHolder.binding.textLikes.setText(activity.resources.getString(R.string.HOWMANYLIKE_MORETHAN0))

                } else {

                    viewHolder.binding.textLikeCount.visibility = View.GONE
                    viewHolder.binding.textLikes.setText(activity.resources.getString(R.string.HOWMANYLIKE_0))
                }

                viewHolder.binding.textTime.setText(
                    SimpleDateFormat("yyyy년 MM월 dd일").format(Date(story.timestamp))
                )

            }

        }

    }

    override fun getItemCount(): Int { return stories.size }

    fun changeStoris (stories: MutableList<StoryEntity?>) { this.stories = stories }

}