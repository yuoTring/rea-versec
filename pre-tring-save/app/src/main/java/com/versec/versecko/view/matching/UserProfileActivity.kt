package com.versec.versecko.view.matching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.versec.versecko.R
import com.versec.versecko.databinding.ActivityUserProfileBinding
import com.versec.versecko.viewmodel.DetailProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel : DetailProfileViewModel by viewModel<DetailProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)




        binding.buttonAccept.setOnClickListener {




        }

        binding.buttonReject.setOnClickListener {


        }
    }
}