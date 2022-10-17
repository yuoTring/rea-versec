package com.versec.versecko.view.signup.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.databinding.ItemRecyclerPlaceBinding
import com.versec.versecko.databinding.ItemRecyclerSubplaceBinding

class SubPlaceAdapter(

    var activity: Activity,
    var placeList : List<String>,
    var selectedPlace : String,
    var chosenList : MutableList<String>,
    private val onClick : (String?) -> Unit

) : RecyclerView.Adapter<SubPlaceAdapter.ViewHolder>() {

    private var totalOrNot = false

    inner class ViewHolder(val binding : ItemRecyclerSubplaceBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(place : String?, onClick: (String?) -> Unit) {

        binding.buttonChoosePlace.setOnClickListener {
            onClick(place)
        }



        }
    }
    //   class ViewHolder(binding: ItemRecyclerPlaceBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemRecyclerSubplaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(placeList.get(position), onClick)
        holder.binding.buttonChoosePlace.setText(placeList.get(position))
        holder.binding.imageChosenPlace.visibility = View.INVISIBLE

        chosenList.forEach { place ->

            //Residence
            if (selectedPlace.equals("null")) {

                if (place.equals(placeList.get(position)))
                    holder.binding.imageChosenPlace.visibility = View.VISIBLE

            //Trip
            } else {


                if (position == 0) {
                    if (place.equals(placeList.get(position)))
                        holder.binding.imageChosenPlace.visibility = View.VISIBLE

                } else {

                    if (place.equals(selectedPlace+" "+ placeList.get(position)))
                        holder.binding.imageChosenPlace.visibility = View.VISIBLE

                }

            }

        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun updateChosenList (chosenList: MutableList<String>) {

        this.chosenList = chosenList

    }

    fun updateSelectedPlace (selectedPlace: String) {
        this.selectedPlace = selectedPlace
    }




}