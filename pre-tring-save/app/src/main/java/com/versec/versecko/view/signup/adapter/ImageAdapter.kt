package com.versec.versecko.view.signup.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.versec.versecko.databinding.ItemRecyclerImageBinding

class ImageAdapter (

    var imageList : MutableList<Uri>,
    val resourceId : Int,
    val modifyOrNot : Boolean,
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
        holder.binding.textSlotIndex.visibility = View.INVISIBLE

        if (
                !imageList.get(position).toString().equals("---") &&
                !imageList.get(position).toString().equals("null")
        ) {

            Glide
                .with(holder.binding.root)
                .load(imageList.get(position))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .into(holder.binding.buttonAddImage)

        }
        else if (imageList.get(position).toString().equals("null")) {


            holder.binding.buttonAddImage.setImageResource(resourceId)

            if (modifyOrNot) {

                holder.binding.textSlotIndex.visibility = View.VISIBLE
                val index = position + 1
                holder.binding.textSlotIndex.setText("SLOT "+index)
            }
        }
        else {


            holder.binding.buttonAddImage.setImageResource(resourceId)

            if (modifyOrNot) {

                holder.binding.textSlotIndex.visibility = View.VISIBLE
                val index = position + 1
                holder.binding.textSlotIndex.setText("SLOT"+ index)
            }

        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateList (imageList: MutableList<Uri>) {
        this.imageList = imageList
    }

}