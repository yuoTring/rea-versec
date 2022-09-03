package com.versec.versecko.view.signup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.versec.versecko.AppContext
import com.versec.versecko.databinding.ActivityEntryBinding
import com.versec.versecko.util.LocationPermission
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.MainScreenActivity
import com.versec.versecko.view.UserTestActivity

class EntryActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityEntryBinding
    private lateinit var view: View

    //private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        view = binding.root

        setContentView(view)


        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        LocationPermission.requestLocationPermission(this)




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
            //= Intent(this, UserTestActivity::class.java)
            //= Intent(this, ChoosePlaceActivity::class.java)
            = Intent(this, FillUserInfoActivity::class.java)



            startActivity(intent)
        }







    }



}

