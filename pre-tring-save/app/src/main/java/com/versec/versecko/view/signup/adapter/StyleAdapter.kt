package com.versec.versecko.view.signup.adapter

import com.versec.versecko.R
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.databinding.ItemRecyclerStyleContentsBinding

class StyleAdapter (

    private val activity : Activity,
    private val styleList: MutableList<String>,
    private var chosenList: MutableList<String>,

    private val onClick: (String?) -> Unit


) : RecyclerView.Adapter<StyleAdapter.ViewHolder>()
{
    inner class ViewHolder (
        val binding : ItemRecyclerStyleContentsBinding,

    ) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind (style : String?, onClick: (String?) -> Unit) {

            binding.root.setOnClickListener {
                onClick(style)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerStyleContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(styleList.get(position), onClick)


        val resourceId = activity.resources.getIdentifier("s1_test"+styleList.get(position), "drawable", activity.packageName)
        holder.binding.imageStyle.setImageDrawable(ContextCompat.getDrawable(activity, resourceId))
        holder.binding.textStyle.setText(styleList.get(position))

        if (chosenList.contains(styleList.get(position))) {

            holder.binding.imageOutLine.visibility = View.VISIBLE
            holder.binding.textStyle.setTextColor(ContextCompat.getColor(activity, R.color.blue_azure))
        } else {
            holder.binding.imageOutLine.visibility = View.INVISIBLE
            holder.binding.textStyle.setTextColor(ContextCompat.getColor(activity, R.color.black))
        }


    }

    override fun getItemCount(): Int {

        return styleList.size
    }

    fun updateChosenList ( chosenList: MutableList<String>) {
        this.chosenList = chosenList
    }
}