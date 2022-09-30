package com.versec.versecko.view.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityProfileModifyBinding
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.signup.FillUserInfoActivity
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.ProfileModifyViewModel
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime

class ProfileModifyActivity : AppCompatActivity() {

    private lateinit var userEntity: UserEntity

    private val viewModel : ProfileModifyViewModel by viewModel<ProfileModifyViewModel>()

    private lateinit var binding : ActivityProfileModifyBinding
    private lateinit var view : View

    private lateinit var layoutManager: RecyclerView.LayoutManager

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityProfileModifyBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)


        residenceList = mutableListOf("+")
        tripList = mutableListOf("+")
        styleList = mutableListOf("+")

        viewModel._user.observe(this, Observer { updatedUser ->

            userEntity = updatedUser
            Log.d("profile-modify", updatedUser.toString())



            residenceList.add(userEntity.mainResidence)
            tripList.addAll(userEntity.tripWish)
            styleList.addAll(userEntity.tripStyle)

            binding.editSelfIntroduction.setText(userEntity.selfIntroduction)



        })





        layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL, false)

        binding.buttonBack.setOnClickListener { finish() }

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)




        val layoutManagerResidence: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerTrip
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerStyle
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapterResidence = TagAdapter(residenceList) { place ->

            if (place.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.RESIDENCE
                ), FillUserInfoActivity.RESIDENCE
                )

            residenceList.removeAll(residenceList)
            residenceList.add("+")
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()



        }

        binding.editSelfIntroduction.doAfterTextChanged {
                text ->

            var textCount = "0/200"

            if (text.toString().length>20) {

            }
            else
            {

                textCount=
                    text.toString().length.toString() + "/200"
                binding.textCheckLength.setText(textCount)
            }


        }

        binding.recyclerChosenResidence.layoutManager = layoutManagerResidence
        binding.recyclerChosenResidence.adapter = adapterResidence

        adapterTrip = TagAdapter(tripList) { trip ->

            if (trip.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.TRIP
                ), FillUserInfoActivity.TRIP
                )
            else {
                tripList.remove(trip)
                adapterTrip.updateTagList(tripList)
                adapterTrip.notifyDataSetChanged()
            }

        }

        binding.recyclerChosenTripWish.layoutManager = layoutManagerTrip
        binding.recyclerChosenTripWish.adapter = adapterTrip

        binding.recyclerChosenStyle.layoutManager = layoutManagerStyle


        binding.buttonComplete.setOnClickListener {

            var checkImageReadyOrNot = false



            if (checkImageReadyOrNot) {


                userEntity.selfIntroduction = binding.editSelfIntroduction.text.toString()





            }
            else
            {
                Toast.makeText(this,"nonono", Toast.LENGTH_SHORT).show()
            }





        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == FillUserInfoActivity.RESIDENCE && data != null) {
            residenceList.removeAll(residenceList)
            residenceList.add("+")
            residenceList.addAll(data.getStringArrayListExtra("residence")!!)
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

            userEntity.mainResidence = residenceList.get(1)
        }
        else if (requestCode == FillUserInfoActivity.RESIDENCE && data == null) {

            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()

        }
        else if (requestCode == FillUserInfoActivity.TRIP && data != null) {

            tripList.removeAll(tripList)
            tripList.add("+")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()

            userEntity.tripWish.addAll(tripList)

        }
        else if (requestCode == FillUserInfoActivity.TRIP && data == null) {
            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()

        }
        else {
            Toast.makeText(this, "4", Toast.LENGTH_SHORT).show()

        }






    }
}