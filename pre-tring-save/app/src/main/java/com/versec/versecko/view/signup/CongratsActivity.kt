package com.versec.versecko.view.signup

import android.content.Intent
import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.versec.versecko.view.MainScreenActivity
import kotlinx.coroutines.*

class CongratsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)



        CoroutineScope(Dispatchers.Main).launch {

            delay(2500)

            startActivity(Intent(this@CongratsActivity, MainScreenActivity::class.java))



        }
    }

    override fun onBackPressed() {


    }
}