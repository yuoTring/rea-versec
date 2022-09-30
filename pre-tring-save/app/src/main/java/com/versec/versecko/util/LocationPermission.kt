package com.versec.versecko.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.versec.versecko.AppContext
import kotlinx.coroutines.*


class LocationPermission {

    companion object {

        @RequiresApi(Build.VERSION_CODES.N)

        fun requestLocationPermission (activity: AppCompatActivity) {

            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

            val locationPermissionRequest = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->

                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                            AppContext.latitude = Math.abs(location.latitude)
                            AppContext.longitude = Math.abs(location.longitude)
                        }
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                    }
                    else -> {


                        CoroutineScope(Dispatchers.Main).launch {

                            Toast.makeText(activity, "To use this service, You MUST turn on Your location permission!", Toast.LENGTH_SHORT).show()
                            delay(2000)
                            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                        }




                    }

                }


            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                when {
                    ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    -> {
                        //Toast.makeText(activity, "When!!!", Toast.LENGTH_SHORT).show()

                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        location ->

                            //Toast.makeText(activity, "location: "+ location.toString(), Toast.LENGTH_SHORT).show()
                            AppContext.latitude = Math.abs(location.latitude)
                            AppContext.longitude = Math.abs(location.longitude)



                        }
                    }
                    activity.shouldShowRequestPermissionRationale("!!!") -> {

                    }
                    else -> {
                        locationPermissionRequest.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }

                }
            }

        }



    }



}