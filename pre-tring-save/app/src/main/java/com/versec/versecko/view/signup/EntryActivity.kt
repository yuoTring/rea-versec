package com.versec.versecko.view.signup

import android.content.Intent
import android.net.Uri
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

            user.mainResidence = "??????"
            user.subResidence = "??????"

            if (x % 2 == 1)
                user.tripWish = mutableListOf("?????? ?????????", "?????? ??????","?????? ??????")
            else if (x % 2 == 0)
                user.tripWish = mutableListOf("?????? ?????????", "?????? ?????????","?????? ??????")

            user.uriMap = mutableMapOf("0" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=5d937e0a-6cbd-410c-bf94-7ee07b461a16")

            viewModel.insert(user)
        } **/









        binding.textServiceTerms.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/ver-1-0-0-c7ff915f2e4946ba82ec99241baebb51")))
        }

        binding.textPersonalInfoTerms.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/Ver-1-0-0-fc7683ead4c04cbd96060dfecf8769e7")))
        }

        binding.textLocationTerms.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/Ver-1-0-6dda9ec63e5748bd801a8ccb73948d4e")))
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

