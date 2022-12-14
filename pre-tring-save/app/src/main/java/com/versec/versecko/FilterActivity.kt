package com.versec.versecko

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.versec.versecko.databinding.ActivityFilterBinding
import com.versec.versecko.viewmodel.FilterViewModel
import com.versec.versecko.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterActivity : AppCompatActivity() {



    private val viewModel : FilterViewModel by viewModel<FilterViewModel>()

    private lateinit var binding : ActivityFilterBinding


    private lateinit var genderValue : String

    private lateinit var minAgeValue: String
    private lateinit var maxAgeValue: String
    private lateinit var distanceValue : String

    private lateinit var genderValueOld : String
    private lateinit var minAgeValueOld : String
    private lateinit var maxAgeValueOld : String
    private lateinit var distanceValueOld : String

    companion object {

        private const val FILTER_UPDATED = 805
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)




        init()

    }



    fun init () {

        //Gender
        if (viewModel.getGender().isNullOrBlank()) {

            genderValue = "both"
            genderValueOld = genderValue

            viewModel.setGender(genderValue)

            setGenderUI(genderValue)




        } else {

            genderValue = viewModel.getGender()!!
            genderValueOld = genderValue

            setGenderUI(genderValue)
        }


        //Age
        if (viewModel.getAgeRange().isNullOrEmpty()) {

            minAgeValue = "20"
            maxAgeValue = "80"

            minAgeValueOld = minAgeValue
            maxAgeValueOld = maxAgeValue

            viewModel.setAgeRange(minAgeValue.toInt(), maxAgeValue.toInt())

            setAgeRangeUI(minAgeValue, maxAgeValue)

        } else {

            val list =
                viewModel.getAgeRange()

            minAgeValue = list!!.get(0).toString()
            maxAgeValue = list!!.get(1).toString()

            minAgeValueOld = minAgeValue
            maxAgeValueOld = maxAgeValue

            setAgeRangeUI(minAgeValue, maxAgeValue)

        }

        if (viewModel.getDistance() == null) {

            distanceValue = "20"
            distanceValueOld = distanceValue

            viewModel.setDistance(distanceValue.toInt())

            setDistanceUI(distanceValue)
        } else {

            distanceValue = viewModel.getDistance().toString()
            distanceValueOld = distanceValue

            setDistanceUI(distanceValue)
        }

        binding.buttonGenderMale.setOnClickListener {
            genderValue = "male"
            setGenderUI(genderValue)
        }
        binding.buttonGenderFemale.setOnClickListener {
            genderValue = "female"
            setGenderUI(genderValue)
        }
        binding.buttonGenderBoth.setOnClickListener {
            genderValue = "both"
            setGenderUI(genderValue)
        }

        binding.sliderAge.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {

            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {

                minAgeValue = slider.values.get(0).toInt().toString()
                maxAgeValue = slider.values.get(1).toInt().toString()

                setAgeRangeUI(minAgeValue, maxAgeValue)
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {

                minAgeValue = slider.values.get(0).toInt().toString()
                maxAgeValue = slider.values.get(1).toInt().toString()

                setAgeRangeUI(minAgeValue, maxAgeValue)
            }


        })

        binding.sliderDistance.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {

            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                distanceValue = slider.value.toInt().toString()
                setDistanceUI(distanceValue)
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {

                distanceValue = slider.value.toInt().toString()
                setDistanceUI(distanceValue)
            }

        })

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonSet.setOnClickListener {


            if (
                        genderValueOld.equals(genderValue) &&
                        minAgeValueOld.equals(minAgeValue) &&
                        maxAgeValueOld.equals(maxAgeValue) &&
                        distanceValueOld.equals(distanceValue)
            ) {
                finish()

            } else {

                viewModel.setGender(genderValue)
                viewModel.setAgeRange(minAgeValue.toInt(), maxAgeValue.toInt())
                viewModel.setDistance(distanceValue.toInt())

                setResult(FILTER_UPDATED)
                finish()

            }

            finish()
        }
    }




    private fun setGenderUI (gender : String) {

        if (gender.equals("male")) {

            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_12_charcoal)
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_12_superlightgray)
            binding.buttonGenderBoth.setBackgroundResource(R.drawable.button_corner_12_superlightgray)

        } else if (gender.equals("female")) {

            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_12_superlightgray)
            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_12_charcoal)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.buttonGenderBoth.setBackgroundResource(R.drawable.button_corner_12_superlightgray)

        } else {

            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_12_superlightgray)
            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_12_superlightgray)
            binding.buttonGenderBoth.setBackgroundResource(R.drawable.button_corner_12_charcoal)
            binding.buttonGenderBoth.setTextColor(ContextCompat.getColor(this, R.color.white))

        }
    }

    private fun setAgeRangeUI (minAgeValue : String, maxAgeValue : String ) {

        binding.textAgeRange.setText(minAgeValue+"??? - "+maxAgeValue+"???")
        binding.sliderAge.values = mutableListOf(minAgeValue.toFloat(), maxAgeValue.toFloat())
    }

    private fun setDistanceUI (distance : String) {

        binding.textDistance.setText(distance+"km ??????")
        binding.sliderDistance.value = distance.toFloat()
    }
}