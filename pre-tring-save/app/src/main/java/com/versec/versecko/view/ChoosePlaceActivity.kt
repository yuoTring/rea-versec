package com.versec.versecko.view

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.databinding.ActivityChoosePlaceBinding
import com.versec.versecko.view.signup.adapter.PlaceAdapter

class ChoosePlaceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChoosePlaceBinding
    private lateinit var view: View
    private lateinit var adapter: PlaceAdapter

    private lateinit var placeList: List<String>
    private lateinit var chosenList : MutableList<String>

    private var requestCode : Int =0

    /**
     * residence -> 1
     * trip -> 2
     */



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val intent = intent
        requestCode = intent.getIntExtra("requestCode",0)



        binding = ActivityChoosePlaceBinding.inflate(layoutInflater)

        view= binding.root

        setContentView(view)

        placeList = resources.getStringArray(R.array.place_main).toList()

        chosenList = mutableListOf()


        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapter = PlaceAdapter(placeList, chosenList ) { place ->

            //residence
            if (requestCode == 1) {
                chosenList.removeAll(chosenList)

                if (place != null) {
                    chosenList.add(place)
                }
            }
            //trip
            else if (requestCode ==2 ) {

                // already chosen -> delete
                if (chosenList.contains(place)) {
                    chosenList.remove(place)
                }
                // add
                else {

                    if (place != null) {
                        chosenList.add(place)
                    }

                }
            }

            adapter.updateChosenList(chosenList)
            adapter.notifyDataSetChanged()


            Toast.makeText(this, chosenList.toString(), Toast.LENGTH_SHORT).show()




        }

        binding.recyclerMainResidence.layoutManager=layoutManager
        binding.recyclerMainResidence.adapter = adapter



        binding.buttonSet.setOnClickListener {



            val intent = Intent()
            if (requestCode==1) {
                intent.putStringArrayListExtra("residence", ArrayList(chosenList))
                setResult(100, intent)

            }
            else if (requestCode ==2 ) {

                intent.putStringArrayListExtra("trip", ArrayList(chosenList))
                setResult(200, intent)

            }
            finish()

        }
        binding.buttonBack.setOnClickListener { finish() }




    }

    override fun onDestroy() {
        super.onDestroy()

    }
}