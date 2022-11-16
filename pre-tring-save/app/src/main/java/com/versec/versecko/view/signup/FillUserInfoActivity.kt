package com.versec.versecko.view.signup

import android.annotation.SuppressLint
import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityFillUserInfoBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.ChooseStyleActivity
import com.versec.versecko.view.signup.adapter.StyleAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.FillUserInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern

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

    private val regex = Pattern.compile("[!@#$%^&*|/?,.:;{'\"<>}(=+)-]")



    companion object {

        const val RESIDENCE = 1
        const val TRIP = 2
        const val STYLE = 3

    }





    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityFillUserInfoBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        val intent = intent

        user = UserEntity()
        user.phoneNumber = intent.getStringExtra("phoneNumber").toString()

        residenceList = mutableListOf("+")
        tripList = mutableListOf("+")
        styleList = mutableListOf("+")

        binding.editNickName.doAfterTextChanged { text ->

            val length = text!!.length

            if (length > 16) {

                binding.editNickName.text.delete(length-1, length)
                Toast.makeText(this, "최대 16자의 닉네임만 사용 가능합니다!", Toast.LENGTH_SHORT).show()

            }
        }



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
            binding.textCheckBirth.visibility = View.VISIBLE



            if (valid)
            {
                binding.textCheckBirth.setText("유효한 생년월일입니다.")
                binding.textCheckBirth.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity,R.color.blue))
                user.birth = text.toString()
                checkBirth =true
            }
            else
            {
                binding.textCheckBirth.setText("잘못된 생년월일입니다.")
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

            if (place.equals("다시 설정") || place.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode", RESIDENCE), RESIDENCE)

            residenceList.removeAll(residenceList)
            residenceList.add("다시 설정")
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

            if (regex.matcher(binding.editNickName.text.toString()).find()) {

                Toast.makeText(this, "특수 문자는 닉네임에 사용 불가능합니다!", Toast.LENGTH_SHORT).show()

                binding.textCheckNickNameOverlap.setText("사용 불가능한 닉네임입니다!")
                binding.textCheckNickNameOverlap.setTextColor(ContextCompat.getColor(this, R.color.red))
                checkNickname = false

            } else {

                binding.textCheckNickNameOverlap.visibility = View.VISIBLE


                viewModel.checkNickName(binding.editNickName.text.toString()).observe(this, androidx.lifecycle.Observer {
                        result ->

                    if (result.equals(Response.Exist(1))) {

                        binding.textCheckNickNameOverlap.setText("이미 존재하는 닉네임입니다!")
                        binding.textCheckNickNameOverlap.setTextColor(ContextCompat.getColor(this, R.color.red))
                        checkNickname = false
                    }
                    else {
                        binding.textCheckNickNameOverlap.setText("사용 가능한 닉네임입니다!")
                        user.nickName = binding.editNickName.text.toString()
                        binding.textCheckNickNameOverlap.setTextColor(ContextCompat.getColor(this, R.color.blue))
                        checkNickname = true

                    }
                })

            }


        }

        adapterStyle = TagAdapter(styleList) { style ->

            if (style.equals("+"))
                startActivityForResult(Intent(this, ChooseStyleActivity::class.java).putExtra("requestCode", STYLE), STYLE)
            else {
                styleList.remove(style)
                adapterStyle.updateTagList(styleList)
                adapterStyle.notifyDataSetChanged()
            }
        }

        binding.recyclerChosenStyle.layoutManager = layoutManagerStyle
        binding.recyclerChosenStyle.adapter = adapterStyle


        binding.buttonToNext.setOnClickListener {


            if (checkNickname && checkBirth && checkGender && residenceList.size>1 && tripList.size>1 && styleList.size>1) {

                user.nickName = binding.editNickName.text.toString()
                user.birth = binding.editBirth.text.toString()



                user.mainResidence = residenceList.get(1)
                if (residenceList.size>2)
                    user.subResidence = residenceList.get(2)
                else
                    user.subResidence = residenceList.get(1)

                user.tripWish.addAll(tripList.subList(1, tripList.size))
                user.tripStyle.addAll(styleList.subList(1, styleList.size))

                user.latitude = AppContext.latitude
                user.longitude = AppContext.longitude
                user.geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(user.latitude, user.longitude))

                var intent = Intent(this, FillUserImageActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)

            } else {

                Toast.makeText(this, "모든 정보를 입력하고 다음 버튼을 눌러주세요!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    //0123 45 67

    private fun checkValidBirth (birth : String) : Boolean {

        if (birth.length < 8 || birth.length > 8)
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

    override fun onBackPressed() {}

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIDENCE && data != null) {

            residenceList.clear()
            residenceList.add("다시 설정")
            residenceList.addAll(data.getStringArrayListExtra("residence")!!)

            Log.d("tag-check", "request: "+ RESIDENCE+"__"+residenceList.toString())
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

        }
        else if (requestCode == RESIDENCE && data == null) {

            residenceList.clear()
            residenceList.add("+")

            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

        }
        else if (requestCode == TRIP && data != null) {

            tripList.clear()
            tripList.add("다시 설정")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()


        }
        else if (requestCode == TRIP && data == null) {

            tripList.clear()
            tripList.add("+")

            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()
        }
        else if (requestCode == STYLE && data != null) {

            styleList.clear()
            styleList.add("다시 설정")
            styleList.addAll(data.getStringArrayListExtra("style")!!)
            adapterStyle.updateTagList(styleList)
            adapterStyle.notifyDataSetChanged()

        }
        else if (requestCode == STYLE && data == null) {

            styleList.clear()
            styleList.add("+")

            adapterStyle.updateTagList(styleList)
            adapterStyle.notifyDataSetChanged()
        }


    }
}