package com.versec.versecko.view.signup

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityFillUserInfoBinding
import com.versec.versecko.view.signup.adapter.TagAdapter
import java.util.*
import java.util.concurrent.TimeUnit

class FillUserInfoActivity : AppCompatActivity()
{

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
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.black))
            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_8_superlightgray)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.gray))


            user.gender = "male"
        }
        binding.buttonGenderFemale.setOnClickListener {

            binding.buttonGenderFemale.setBackgroundResource(R.drawable.button_corner_8_black)
            binding.buttonGenderFemale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.black))
            binding.buttonGenderMale.setBackgroundResource(R.drawable.button_corner_8_superlightgray)
            binding.buttonGenderMale.setTextColor(ContextCompat.getColor(this@FillUserInfoActivity, R.color.gray))

            user.gender = "female"
        }


        binding.buttonToNext.setOnClickListener {

            user.nickName = binding.editNickName.text.toString()
            user.birth = binding.editBirth.text.toString()
            user.age = user.birth.substring(0,4).toInt() - Calendar.YEAR
        }

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapterResidence = TagAdapter(residenceList) { plus ->
            

        }





    }

    private fun checkValidBirth () {

    }
}