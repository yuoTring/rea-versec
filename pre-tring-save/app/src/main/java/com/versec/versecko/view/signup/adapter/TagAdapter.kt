package com.versec.versecko.view.signup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.databinding.ItemRecyclerTagBinding

class TagAdapter (

    var tagList: MutableList<String>,
    //val tagList : MutableList<String>,
    //val chosenList: MutableList<String>,
    private val onClick: (String?) -> Unit

        ): RecyclerView.Adapter<TagAdapter.ViewHolder>() {


    inner class ViewHolder (val binding: ItemRecyclerTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag : String?, onClick : (String?) -> Unit) {



            binding.buttonAddTag.setOnClickListener {
                onClick(tag)
            }

            binding.buttonDeleteTag.setOnClickListener {
                onClick(tag)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(tagList.get(position), onClick)
        holder.binding.buttonAddTag.setText(tagList.get(position))
    }

    override fun getItemCount(): Int {

        return tagList.size
    }

    fun updateTagList (tagList: MutableList<String>) {
        this.tagList = tagList
    }
}

/**
 *
 * class TagAdapter (

var tagList: MutableList<String>,
//val chosenList: MutableList<String>,
private val onClick: (String?, Boolean) -> Unit

): RecyclerView.Adapter<TagAdapter.ViewHolder>() {


inner class ViewHolder (val binding: ItemRecyclerTagBinding) : RecyclerView.ViewHolder(binding.root) {
fun bind(tag: String?, delete: Boolean, onClick: (String?, Boolean) -> Unit) {
binding.buttonAddTag.setOnClickListener {
this@TagAdapter.onClick(tag, delete)
}

binding.buttonDeleteTag.setOnClickListener {
this@TagAdapter.onClick(tag, delete)
}

}
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

val binding = ItemRecyclerTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)

return ViewHolder(binding)
}

override fun onBindViewHolder(holder: ViewHolder, position: Int) {

with(holder) {

holder.bind(tagList.get(position), true, onClick)

holder.binding.buttonAddTag.setText(tagList.get(position))

}
}

override fun getItemCount(): Int {

return tagList.size
}

fun updateTagList (tagList: MutableList<String>) {
this.tagList = tagList
}
}
 */