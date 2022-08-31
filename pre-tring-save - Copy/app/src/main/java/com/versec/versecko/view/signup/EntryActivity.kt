package com.versec.versecko.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.versec.versecko.R

class EntryActivity : AppCompatActivity()
{

    private lateinit var textServiceTerms : TextView
    private lateinit var textPersonalInfoTerms : TextView
    private lateinit var textLocationTerms : TextView
    private lateinit var buttonAgreeAndStart : AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        textServiceTerms = findViewById(R.id.textServiceTerms)
        textPersonalInfoTerms = findViewById(R.id.textPersonalInfoTerms)
        textLocationTerms = findViewById(R.id.textLocationTerms)
        buttonAgreeAndStart = findViewById(R.id.buttonAgreeAndStart)

        textServiceTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        }

        textPersonalInfoTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        }

        textLocationTerms.setOnClickListener {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        }

        buttonAgreeAndStart.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}