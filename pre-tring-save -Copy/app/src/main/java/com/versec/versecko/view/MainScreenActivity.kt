package com.versec.versecko.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.versec.versecko.R
import com.versec.versecko.databinding.ActivityMainScreenBinding
import com.versec.versecko.view.profile.ProfileFragment

class MainScreenActivity : AppCompatActivity() {


    /**
     * private lateinit var binding : ActivityMainScreenBinding
    private lateinit var view : View

    private lateinit var matchingFragment: MatchingFragment
    private lateinit var profileFragment: ProfileFragment
     */

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var view : View
    //private lateinit var matchingFragment: MatchingFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (savedInstanceState == null) {

                //add(R.id.fragmentContainer, MatchingFragment.newInstance(),"matching")
                //addToBackStack(null)
                add(R.id.fragmentContainer, ProfileFragment.newInstance(), "profile")
                addToBackStack(null)




            }


        }




        binding.bottomNavigationBar.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {


                when(item.itemId) {
                    R.id.page_home -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)


                            //show(matchingFragment)
                            hide(profileFragment)
                        }

                    }

                    R.id.page_discovery -> {
                        Toast.makeText(this@MainScreenActivity, "!!!", Toast.LENGTH_SHORT).show()

                    }

                    R.id.page_lounge -> {
                        Toast.makeText(this@MainScreenActivity, "!!!", Toast.LENGTH_SHORT).show()

                    }

                    R.id.page_chat -> {
                        Toast.makeText(this@MainScreenActivity, "!!!", Toast.LENGTH_SHORT).show()

                    }

                    R.id.page_profile -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(profileFragment)
                            //hide(matchingFragment)
                        }



                    }
                }

                return true
            }

        })


    }

    override fun onStart() {
        super.onStart()

        //matchingFragment = supportFragmentManager.findFragmentByTag("matching") as MatchingFragment
        profileFragment = supportFragmentManager.findFragmentByTag("profile") as ProfileFragment
    }
}