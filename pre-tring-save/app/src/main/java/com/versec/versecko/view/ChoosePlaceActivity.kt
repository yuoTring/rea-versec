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
import com.versec.versecko.view.signup.adapter.SubPlaceAdapter
import java.util.function.Predicate

class ChoosePlaceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChoosePlaceBinding
    private lateinit var view: View
    private lateinit var adapter: PlaceAdapter
    private lateinit var subAdapter : SubPlaceAdapter

    private lateinit var placeList: List<String>
    private lateinit var subPlaceList : MutableList<String>
    private lateinit var selectedPlace : String
    private lateinit var chosenList : MutableList<String>


    private var requestCode : Int =0

    /**
     * residence -> 1
     * trip -> 2
     */

    companion object {

        const val RESIDENCE = 1
        const val TRIP = 2

        const val DISCOVERY_RESIDENCE = 501
        const val DISCOVERY_TRIP = 502
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val intent = intent
        requestCode = intent.getIntExtra("requestCode",0)



        binding = ActivityChoosePlaceBinding.inflate(layoutInflater)

        view= binding.root

        setContentView(view)

        placeList = resources.getStringArray(R.array.place_main).toList()
        selectedPlace = "~~~"

        chosenList = mutableListOf()

        adapter = PlaceAdapter(this,placeList, selectedPlace ) { place ->

            selectedPlace = place!!

            Log.d("place-check", selectedPlace)

            adapter.updatePlace(selectedPlace)
            adapter.notifyDataSetChanged()

            val id = resources.getIdentifier("place_"+selectedPlace, "array", packageName)


            subPlaceList = resources.getStringArray(id).toMutableList()

            Log.d("place-check", subPlaceList.toString())


            if (requestCode == RESIDENCE || requestCode == DISCOVERY_RESIDENCE) {

                subPlaceList.removeAt(0)

                subAdapter = SubPlaceAdapter(this,subPlaceList, "null", chosenList) { subPlace ->

                    chosenList.removeAll(chosenList)
                    chosenList.add(subPlace!!)

                    subAdapter.updateChosenList(chosenList)
                    subAdapter.notifyDataSetChanged()
                }

            } else if (requestCode == TRIP || requestCode == DISCOVERY_TRIP) {


                val one = subPlaceList.get(0)

                subPlaceList.removeAll(subPlaceList)
                subPlaceList.add(one)


                subAdapter = SubPlaceAdapter(this,subPlaceList, selectedPlace, chosenList) { subPlace ->

                    var result =
                        selectedPlace +" "+ subPlace

                    if (subPlaceList.indexOf(subPlace) == 0)
                        result = subPlace!!


                    if (!chosenList.contains(result) && chosenList.size < 3) {

                        if (subPlaceList.indexOf(subPlace) == 0) {


                            val temp = mutableListOf<String>()

                            chosenList.forEach {

                                if (it.startsWith(selectedPlace))
                                    temp.add(it)
                            }

                            chosenList.removeAll(temp)

                            chosenList.add(result)

                        } else {

                            var temp = "null"

                            chosenList.forEach {
                                if (it.equals(selectedPlace + " 전체"))
                                    temp = it
                            }

                            chosenList.remove(temp)

                            chosenList.add(result)

                        }

                    }
                    else if (chosenList.contains(result))
                        chosenList.remove(result)
                    else if (chosenList.size > 2)
                        Toast.makeText(this, "최대 3개 지역을 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()


                    subAdapter.updateChosenList(chosenList)
                    subAdapter.notifyDataSetChanged()
                }

            }



            binding.recyclerSubResidence.layoutManager = LinearLayoutManager(this)
            binding.recyclerSubResidence.adapter = subAdapter


        }

        binding.recyclerMainResidence.layoutManager= LinearLayoutManager(this)
        binding.recyclerMainResidence.adapter = adapter



        binding.buttonSet.setOnClickListener {



            val intent = Intent()
            if (requestCode == RESIDENCE) {

                if (chosenList.size>0) {

                    chosenList.add(0, selectedPlace)
                    intent.putStringArrayListExtra("residence", ArrayList(chosenList))

                }
                else {
                    intent.putStringArrayListExtra("residence", ArrayList(listOf(selectedPlace)))
                }

                setResult(100, intent)

            } else if (chosenList.size>0 && requestCode == TRIP) {

                intent.putStringArrayListExtra("trip", ArrayList(chosenList))
                setResult(200, intent)
            } else if (chosenList.size>0 && requestCode == DISCOVERY_RESIDENCE) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
                setResult(400, intent)

            } else if (chosenList.size>0 && requestCode == DISCOVERY_TRIP) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
                setResult(400, intent)
            }

            finish()

        }
        binding.buttonBack.setOnClickListener { finish() }




    }

    override fun onDestroy() {
        super.onDestroy()

    }
}