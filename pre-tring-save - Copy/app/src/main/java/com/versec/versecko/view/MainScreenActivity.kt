package com.versec.versecko.view

import com.versec.versecko.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.versec.versecko.view.matching.MatchingFragment
import com.versec.versecko.view.profile.ProfileFragment

class MainScreenActivity : AppCompatActivity()
{
    //private lateinit var fragmentTransaction: FragmentTransaction

    private lateinit var matchingFragment: MatchingFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        //fragmentTransaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (savedInstanceState == null) {

                add<MatchingFragment>(R.id.fragmentContainer)
                addToBackStack("matching")

                add<ProfileFragment>(R.id.fragmentContainer)
                addToBackStack("profile")

            }




        }



    }
}