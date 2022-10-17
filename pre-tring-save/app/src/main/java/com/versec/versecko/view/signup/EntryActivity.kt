package com.versec.versecko.view.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityEntryBinding
import com.versec.versecko.util.LocationPermission
import com.versec.versecko.util.WindowEventManager
import com.versec.versecko.view.ChooseStyleActivity
import com.versec.versecko.view.*
import com.versec.versecko.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EntryActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityEntryBinding
    private lateinit var view: View

    private val viewModel : ProfileViewModel by viewModel<ProfileViewModel>()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        view = binding.root

        setContentView(view)



        LocationPermission.requestLocationPermission(this)

        lifecycleScope.launch {


            viewModel.getPath()
        }




        /**
        viewModel.userRemote.observe(this, Observer {

            Log.d("user-get", it.uid)
            it.loungeStatus = 0

        })**/


        /**
        val list = mutableListOf<Int>()

        for (x in 2023 .. 2030) {
            list.add(x)
        }


        list.forEach { x->

            val user = UserEntity()

            user.uid = "d_uid_"+x
            user.nickName = "nick_"+x
            user.phoneNumber = "010-"+x+"-0000"
            user.gender = "female"
            user.age = 20

            user.mainResidence = "서울"
            user.subResidence = "서울"

            if (x % 2 == 1)
                user.tripWish = mutableListOf("경기 안산시", "부산 동구","서울 전체")
            else if (x % 2 == 0)
                user.tripWish = mutableListOf("경기 안양시", "경기 시흥시","부산 전체")

            user.uriMap = mutableMapOf("0" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=5d937e0a-6cbd-410c-bf94-7ee07b461a16")

            viewModel.insert(user)
        } **/









        binding.textServiceTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()

        }

        binding.textPersonalInfoTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        }

        binding.textLocationTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        }

        binding.buttonAgreeAndStart.setOnClickListener {
            val intent
            = Intent(this, SignInActivity::class.java)
            //= Intent(this, MainScreenActivity::class.java)
            //= Intent(this, ChoosePlaceActivity::class.java)
            //= Intent(this, FillUserInfoActivity::class.java)
            //= Intent(this, FillUserImageActivity::class.java)
            //= Intent(this, ChooseStyleActivity::class.java)




            startActivity(intent)
        }







    }

    override fun onBackPressed() {

    }



}

