package com.versec.versecko.view

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.versec.versecko.databinding.ActivityChoosePlaceBinding
import com.versec.versecko.view.signup.adapter.PlaceAdapter
import com.versec.versecko.view.signup.adapter.SubPlaceAdapter

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
        const val STORY_FEED = 503
        private const val STORY_UPLOAD = 250


        const val AGAIN_RESIDENCE = 1001
        const val AGAIN_TRIP = 1002

        private const val CANCEL = -1000
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


            adapter.updatePlace(selectedPlace)
            adapter.notifyDataSetChanged()

            val id = resources.getIdentifier("place_"+selectedPlace, "array", packageName)


            subPlaceList = resources.getStringArray(id).toMutableList()



            if (requestCode == RESIDENCE || requestCode == DISCOVERY_RESIDENCE || requestCode == AGAIN_RESIDENCE ) {

                subPlaceList.removeAt(0)

                subAdapter = SubPlaceAdapter(this,subPlaceList, "null", chosenList) { subPlace ->

                    chosenList.clear()
                    chosenList.add(subPlace!!)

                    subAdapter.updateChosenList(chosenList)
                    subAdapter.notifyDataSetChanged()
                }

            } else if (requestCode == TRIP || requestCode == DISCOVERY_TRIP || requestCode == AGAIN_TRIP ) {


                val one = subPlaceList.get(0)

                subPlaceList.clear()
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

            } else if (requestCode == STORY_FEED || requestCode == STORY_UPLOAD ) {

                if (requestCode == STORY_UPLOAD)
                    subPlaceList.removeAt(0)

                subAdapter = SubPlaceAdapter(this, subPlaceList,
                    "null", chosenList) {

                    if (it != null) {


                        chosenList.clear()

                        if (!chosenList.contains(it)) {

                            chosenList.add(selectedPlace)
                            chosenList.add(it)

                        }

                        subAdapter.updateChosenList(chosenList)
                        subAdapter.notifyDataSetChanged()

                    }

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


                finish()

            } else if (chosenList.size>0 && requestCode == TRIP) {

                intent.putStringArrayListExtra("trip", ArrayList(chosenList))
                setResult(200, intent)


                finish()

            } else if (

                (chosenList.size>0 && requestCode == DISCOVERY_RESIDENCE) ||
                (chosenList.size>0 && requestCode == AGAIN_RESIDENCE)

            ) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
                setResult(400, intent)


                finish()

            } else if (

                (chosenList.size>0 && requestCode == DISCOVERY_TRIP) ||
                (chosenList.size>0 && requestCode == AGAIN_TRIP)

            ) {

                intent.putStringArrayListExtra("condition", ArrayList(chosenList))
                setResult(400, intent)


                finish()

            } else if (requestCode == STORY_FEED || requestCode == STORY_UPLOAD) {

                if (chosenList.size > 0 ){

                    if (chosenList.get(1).endsWith("전체")) {

                        Log.d("story-confirm", chosenList.toString())

                        intent.putStringArrayListExtra("story", ArrayList(chosenList))
                        setResult(350, intent)


                        finish()


                    } else {


                        Log.d("story-confirm", chosenList.toString())
                        intent.putStringArrayListExtra("story", ArrayList(chosenList))
                        setResult(300, intent)


                        finish()


                    }




                } else {

                    Toast.makeText(this, "동네 하나를 선택해주세요!", Toast.LENGTH_SHORT).show()
                }

            }


        }
        binding.buttonBack.setOnClickListener {

            if (requestCode == AGAIN_RESIDENCE || requestCode == AGAIN_TRIP) {

                setResult(CANCEL)
                finish()

            } else {

                finish()
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()

    }
}