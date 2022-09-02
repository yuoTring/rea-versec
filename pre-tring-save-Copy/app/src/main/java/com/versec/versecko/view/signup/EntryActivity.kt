package com.versec.versecko.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.versec.versecko.databinding.ActivityEntryBinding
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.MainScreenActivity
import com.versec.versecko.view.UserTestActivity

class EntryActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityEntryBinding
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        view = binding.root

        setContentView(view)


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