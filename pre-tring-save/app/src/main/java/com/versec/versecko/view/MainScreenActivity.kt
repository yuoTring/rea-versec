package com.versec.versecko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.versec.versecko.FilterActivity
import com.versec.versecko.R
import com.versec.versecko.databinding.ActivityMainScreenBinding
import com.versec.versecko.view.chat.ChatFragment
import com.versec.versecko.view.discovery.DiscoveryFragment
import com.versec.versecko.view.matching.MatchingFragment
import com.versec.versecko.view.profile.ProfileFragment
import java.util.ArrayList

class MainScreenActivity : AppCompatActivity() {


    /**
     * private lateinit var binding : ActivityMainScreenBinding
    private lateinit var view : View

    private lateinit var matchingFragment: MatchingFragment
    private lateinit var profileFragment: ProfileFragment
     */

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var view : View
    private lateinit var matchingFragment: MatchingFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var chatFragment : ChatFragment
    private lateinit var discoveryFragment: DiscoveryFragment

    private lateinit var discoveryMatchingFragment: MatchingFragment

    private lateinit var bundle: Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)



        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (savedInstanceState == null) {

                add(R.id.fragmentContainer, MatchingFragment.newInstance(MatchingFragment.MAIN, arrayListOf("~")), "matching")
                addToBackStack(null)
                add(R.id.fragmentContainer, ProfileFragment.newInstance(), "profile")
                addToBackStack(null)
                add(R.id.fragmentContainer, ChatFragment.newInstance(), "chat")
                addToBackStack(null)
                add(R.id.fragmentContainer, DiscoveryFragment.newInstance(), "discovery")
                addToBackStack(null)



            }


        }

        binding.buttonRight2.setOnClickListener { startActivity(Intent(this, FilterActivity::class.java)) }

        binding.bottomNavigationBar.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {


                when(item.itemId) {
                    R.id.page_home -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)


                            show(matchingFragment)
                            hide(discoveryFragment)
                            hide(chatFragment)
                            hide(profileFragment)
                        }

                    }

                    R.id.page_discovery -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)


                            show(discoveryFragment)
                            hide(matchingFragment)
                            hide(chatFragment)
                            hide(profileFragment)

                        }
                    }

                    R.id.page_lounge -> {
                        Toast.makeText(this@MainScreenActivity, "!!!", Toast.LENGTH_SHORT).show()

                    }

                    R.id.page_chat -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(chatFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(profileFragment)
                        }

                    }

                    R.id.page_profile -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(profileFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(chatFragment)
                        }



                    }
                }

                return true
            }

        })


    }

    override fun onStart() {
        super.onStart()

        matchingFragment = supportFragmentManager.findFragmentByTag("matching") as MatchingFragment
        profileFragment = supportFragmentManager.findFragmentByTag("profile") as ProfileFragment
        chatFragment = supportFragmentManager.findFragmentByTag("chat") as ChatFragment
        discoveryFragment = supportFragmentManager.findFragmentByTag("discovery") as DiscoveryFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 500 && data != null) {

            supportFragmentManager.commit {

                setReorderingAllowed(true)

                var fragment = supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment

                if (fragment == null) {

                    val arrayList = data.getStringArrayListExtra("condition")!!

                    add(R.id.fragmentContainer, MatchingFragment.newInstance(MatchingFragment.STYLE, arrayList), "discoveryMatching")
                    addToBackStack(null)

                    fragment =
                        supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment
                }
            }
        }
    }
}