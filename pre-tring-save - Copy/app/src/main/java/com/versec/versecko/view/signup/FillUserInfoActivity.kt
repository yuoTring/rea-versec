package com.versec.versecko.view.signup

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityFillUserInfoBinding
import com.versec.versecko.util.Results
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.FillUserInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class FillUserInfoActivity : AppCompatActivity()
{

    private val viewModel : FillUserInfoViewModel by viewModel<FillUserInfoViewModel>()

    private lateinit var user : UserEntity

    private lateinit var binding : ActivityFillUserInfoBinding
    private lateinit var view: View

    private lateinit var adapterResidence : TagAdapter
    private lateinit var adapterTrip : TagAdapter
    private lateinit var adapterStyle : TagAdapter

    private lateinit var residenceList : MutableList<String>
    private lateinit var tripList: MutableList<String>
    private lateinit var styleList: MutableList<String>

    private var checkNickname : Boolean = false
    private var checkBirth : Boolean = false
    private var checkGender : Boolean = false



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
            checkGender =true
        }
        binding.buttonGenderFemale.setOnClickListener {

            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_8_black)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.white))
            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_8_superlightgray)
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.gray))

            user.gender = "female"
            checkGender =true
        }


        binding.editBirth.doAfterTextChanged { text ->

            val valid = checkValidBirth(text.toString())



            if (valid)
            {
                binding.textCheckBirth.setText("valid")
                binding.textCheckBirth.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity,R.color.blue))
                user.birth = text.toString()
                checkBirth =true
            }
            else
            {
                binding.textCheckBirth.setText("invalid")
                binding.textCheckBirth.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity,R.color.red))
                checkBirth =false
            }



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
                    binding.textCheckNickNameOverlap.setTextColor(ContextCompat.getColor(this, R.color.red))
                    checkNickname = false
                }
                else {
                    binding.textCheckNickNameOverlap.setText("can")
                    user.nickName = binding.editNickName.text.toString()
                    binding.textCheckNickNameOverlap.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    checkNickname = true

                }
            })
        }


        binding.buttonToNext.setOnClickListener {

            //user.nickName = binding.editNickName.text.toString()
            //user.birth = binding.editBirth.text.toString()

            //if (user.nickName != null && user.gender != null)
            styleList.addAll(mutableListOf("!!!","???"))


            if (checkNickname && checkBirth && checkGender && residenceList.size>1 && tripList.size>1 && styleList.size>1) {

                /**
                 *
                 */
                user.age = user.birth.substring(0,4).toInt() - Calendar.YEAR
                user.mainResidence = residenceList.get(1)
                user.tripWish.addAll(tripList)
                user.tripStyle.addAll(styleList)

                user.latitude = AppContext.latitude
                user.longitude = AppContext.longitude
                user.geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(user.latitude, user.longitude))

                var intent = Intent(this, FillUserImageActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)

            } else {

                Toast.makeText(this, "please fill in all info to push next button!", Toast.LENGTH_SHORT).show()
            }
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

        }
        else if (requestCode == RESIDENCE && data == null) {


        }
        else if (requestCode == TRIP && data != null) {

            tripList.removeAll(tripList)
            tripList.add("+")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()


        }
        else if (requestCode == TRIP && data == null) {

        }
        else {

        }


    }
}