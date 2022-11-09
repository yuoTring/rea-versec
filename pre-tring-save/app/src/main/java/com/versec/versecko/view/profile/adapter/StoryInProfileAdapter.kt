package com.versec.versecko.view.profile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.R
import com.versec.versecko.data.entity.StoryEntity
import com.versec.versecko.databinding.ItemRecyclerStoryInprofileBinding

class StoryInProfileAdapter (

    var stories : MutableList<StoryEntity>,
    private val onClick : (Int) -> Unit

    ) : RecyclerView.Adapter<StoryInProfileAdapter.ViewHolder>() {


    inner class ViewHolder (val binding : ItemRecyclerStoryInprofileBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind (position: Int, onClick: (Int) -> Unit) {

            binding.root.setOnClickListener {
                onClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemRecyclerStoryInprofileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position, onClick)

        if (position == 0) {


            holder.binding.imageStory.setImageResource(R.drawable.button_add_white)
            holder.binding.textTitle.visibility = View.INVISIBLE

        } else {

            if (stories.get(position).uriMap.size > 0) {

                Glide
                    .with(holder.binding.root)
                    .load(stories.get(position).uriMap.values.first())
                    .into(holder.binding.imageStory)
            }



            holder.binding.textTitle.visibility = View.VISIBLE
            holder.binding.textTitle.setText(stories.get(position).contents)

        }



    }

    override fun getItemCount(): Int {
        return stories.size
    }

    fun changeStories (stories: MutableList<StoryEntity>) {
        this.stories = stories
    }


}

