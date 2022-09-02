package com.versec.versecko.view.signup

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityFillUserInfoBinding
import com.versec.versecko.util.Results
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.FillInUserInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class FillUserInfoActivity : AppCompatActivity()
{

    private val viewModel : FillInUserInfoViewModel by viewModel<FillInUserInfoViewModel>()

    private lateinit var user : UserEntity

    private lateinit var binding : ActivityFillUserInfoBinding
    private lateinit var view: View

    private lateinit var adapterResidence : TagAdapter
    private lateinit var adapterTrip : TagAdapter
    private lateinit var adapterStyle : TagAdapter

    private lateinit var residenceList : MutableList<String>
    private lateinit var tripList: MutableList<String>
    private lateinit var styleList: MutableList<String>


    companion object {

        const val RESIDENCE = 1
        const val TRIP =2

    }





    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityFillUserInfoBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        user = UserEntity()

        residenceList = mutableListOf("+")
        tripList = mutableListOf("+")
        styleList = mutableListOf("+")



        binding.buttonGenderMale.setOnClickListener {

            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_8_black)
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.white))
            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_8_superlightgray)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.gray))


            user.gender = "male"
        }
        binding.buttonGenderFemale.setOnClickListener {

            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_8_black)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.white))
            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_8_superlightgray)
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.gray))

            user.gender = "female"
        }

        binding.editBirth.addTextChangedListener { object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val valid = checkValidBirth(text.toString())

                Toast.makeText(this@FillUserInfoActivity, "valid: "+valid,Toast.LENGTH_SHORT).show()


                if (valid)
                {
                    binding.textCheckBirth.setText("valid")
                    binding.textCheckBirth.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity,R.color.blue))
                }
                else
                {
                    binding.textCheckBirth.setText("invalid")
                    binding.textCheckBirth.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity,R.color.red))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        } }




        binding.buttonToNext.setOnClickListener {

            //user.nickName = binding.editNickName.text.toString()
            //user.birth = binding.editBirth.text.toString()
            user.age = user.birth.substring(0,4).toInt() - Calendar.YEAR

            //if (user.nickName != null && user.gender != null)
        }

        val layoutManagerResidence: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerTrip
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerStyle
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapterResidence = TagAdapter(residenceList) { place ->

            if (place.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode", RESIDENCE), RESIDENCE)

            residenceList.removeAll(residenceList)
            residenceList.add("+")
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()



        }

        binding.recyclerChosenResidence.layoutManager = layoutManagerResidence
        binding.recyclerChosenResidence.adapter = adapterResidence

        adapterTrip = TagAdapter(tripList) { trip ->

            if (trip.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode", TRIP), TRIP)
            else {
                tripList.remove(trip)
                adapterTrip.updateTagList(tripList)
                adapterTrip.notifyDataSetChanged()
            }

        }

        binding.recyclerChosenTripWish.layoutManager = layoutManagerTrip
        binding.recyclerChosenTripWish.adapter = adapterTrip

        binding.recyclerChosenStyle.layoutManager = layoutManagerStyle
        //binding.recyclerChosenStyle.adapter = adapterStyle


        binding.buttonCheckOverlap.setOnClickListener {

            viewModel.checkNickName(binding.editNickName.text.toString()).observe(this, androidx.lifecycle.Observer {
                result ->

                if (result.equals(Results.Exist(1))) {

                    binding.textCheckNickNameOverlap.setText("can not")
                }
                else {
                    binding.textCheckNickNameOverlap.setText("can")
                    user.nickName = binding.editNickName.text.toString()

                }
            })
        }



    }

    //0123 45 67

    private fun checkValidBirth (birth : String) : Boolean {

        if (birth.length<8)
            return false
        else {

            val year : Int = birth.substring(0,4).toInt()
            val month : Int = birth.substring(4,6).toInt()
            val date : Int = birth.substring(6,8).toInt()

            if (year>1900 && year< 2015 && month>0 && month <13 && date > 0 && date < 32 )
                return true
            else
                return false


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIDENCE && data != null) {
            residenceList.removeAll(residenceList)
            residenceList.add("+")
            residenceList.addAll(data.getStringArrayListExtra("residence")!!)
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

            user.mainResidence = residenceList.get(1)
        }
        else if (requestCode == RESIDENCE && data == null) {

            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()

        }
        else if (requestCode == TRIP && data != null) {

            tripList.removeAll(tripList)
            tripList.add("+")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()

            user.tripWish.addAll(tripList)

        }
        else if (requestCode == TRIP && data == null) {
            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()

        }
        else {
            Toast.makeText(this, "4", Toast.LENGTH_SHORT).show()

        }


    }
}