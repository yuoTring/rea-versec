package com.versec.versecko.view.signup.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.databinding.ItemRecyclerPlaceBinding

class PlaceAdapter(

    val activity : Activity,
    var placeList : List<String>,
    var selectedPlace : String,
    private val onClick : (String?) -> Unit

) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecyclerPlaceBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(place : String?, onClick: (String?) -> Unit) {

        binding.buttonChoosePlace.setOnClickListener {
            onClick(place)
        }



        }
    }
    //   class ViewHolder(binding: ItemRecyclerPlaceBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            holder.bind(placeList.get(position), onClick)
            holder.binding.buttonChoosePlace.setText(placeList.get(position))
            holder.binding.buttonChoosePlace.setBackgroundResource(activity.resources.getIdentifier("gray_superlight", "color", activity.packageName))
            holder.binding.imageChosenPlace.visibility = View.INVISIBLE

            if (selectedPlace.equals(placeList.get(position))) {
                holder.binding.imageChosenPlace.visibility = View.VISIBLE
                holder.binding.buttonChoosePlace.setBackgroundResource(activity.resources.getIdentifier("white", "color", activity.packageName))
            }

        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun updatePlace (place: String) {
        this.selectedPlace = place
    }




}