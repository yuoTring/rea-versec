package com.versec.versecko.view.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityEntryBinding
import com.versec.versecko.util.LocationPermission
import com.versec.versecko.view.ChooseStyleActivity
import com.versec.versecko.view.*
import com.versec.versecko.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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




        viewModel.userRemote.observe(this, Observer {

            it.loungeStatus = 0
            viewModel.insertUser_Local(it)

            Toast.makeText(this, "@@@", Toast.LENGTH_SHORT).show()

        })









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
            //= Intent(this, SignInActivity::class.java)
            //= Intent(this, MainScreenActivity::class.java)
            //= Intent(this, ChoosePlaceActivity::class.java)
            = Intent(this, FillUserInfoActivity::class.java)
            //= Intent(this, FillUserImageActivity::class.java)
            //= Intent(this, ChooseStyleActivity::class.java)





            startActivity(intent)
        }







    }



}

