package com.versec.versecko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.versec.versecko.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var biding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }
}