package com.versec.versecko.view.discovery.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.versec.versecko.databinding.ItemRecyclerDiscoveryBinding
import com.versec.versecko.view.discovery.Banner

class DiscoveryMainAdapter (

    private val activity : Activity,
    private var list: MutableList<Banner>,
    private var onClick : (String?) -> Unit

        ) : RecyclerView.Adapter<DiscoveryMainAdapter.ViewHolder>() {

    inner class ViewHolder (val binding : ItemRecyclerDiscoveryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (content : String?, onClick: (String?) -> Unit) {

            binding.root.setOnClickListener {
                onClick(content)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerDiscoveryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val banner = list.get(position)

        val imageId =
            activity.resources.getIdentifier(banner.image, "drawable", activity.packageName)

        holder.binding.imageMain.setImageResource(imageId)

        holder.binding.textTitle.setText(banner.title)
        holder.binding.textContents.setText(banner.contents)


        holder.bind(banner.title, onClick)

    }

    override fun getItemCount(): Int { return list.size   }
}