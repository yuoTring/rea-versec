package com.versec.versecko.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.versec.versecko.R
import com.versec.versecko.viewmodel.ProfileModifyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileModifyActivity : AppCompatActivity() {

    private val viewModel : ProfileModifyViewModel by viewModel<ProfileModifyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_modify)

    }
}