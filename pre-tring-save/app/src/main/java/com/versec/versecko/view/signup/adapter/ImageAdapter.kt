package com.versec.versecko.view.signup.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.versec.versecko.databinding.ItemRecyclerImageBinding

class ImageAdapter (

    var imageList : MutableList<Uri>,
    val resourceId : Int,
    private val onClick : (String?, Int) -> Unit

        ) : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){


    inner class ViewHolder (val binding : ItemRecyclerImageBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item : String?, position: Int, onClick: (String?, Int) -> Unit) {

            binding.buttonAddImage.setOnClickListener {
                onClick(item, position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(imageList.get(position).toString(), position, onClick)

        if (!imageList.get(position).toString().equals("---")) {

            Glide
                .with(holder.binding.root)
                .load(imageList.get(position))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .into(holder.binding.buttonAddImage)

        }
        else {

            holder.binding.buttonAddImage.setImageResource(resourceId)

        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateList (imageList: MutableList<Uri>) {
        this.imageList = imageList
    }

}