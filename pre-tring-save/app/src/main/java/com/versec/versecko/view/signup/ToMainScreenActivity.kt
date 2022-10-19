package com.versec.versecko.view.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.versec.versecko.R
import com.versec.versecko.util.LocationPermission
import com.versec.versecko.view.MainScreenActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToMainScreenActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_main_screen)

        LocationPermission.requestLocationPermission(this)

        lifecycleScope.launch {

            delay(2500)



            startActivity(Intent(this@ToMainScreenActivity, MainScreenActivity::class.java))
        }
    }
}