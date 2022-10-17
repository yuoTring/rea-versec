package com.versec.versecko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import com.versec.versecko.FilterActivity
import com.versec.versecko.R
import com.versec.versecko.SettingActivity
import com.versec.versecko.databinding.ActivityMainScreenBinding
import com.versec.versecko.view.chat.ChatFragment
import com.versec.versecko.view.discovery.DiscoveryFragment
import com.versec.versecko.view.matching.MatchingFragment
import com.versec.versecko.view.profile.ProfileFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private var discoveryMatchingFragment : MatchingFragment? = null
    private var discoveryMatchingValue = 0


    private var profileOnOrNot = false
    private var appClose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)



        supportFragmentManager.commit {
            setReorderingAllowed(true)

            if (savedInstanceState == null) {

                add(R.id.fragmentContainer, ProfileFragment.newInstance(), "profile")
                addToBackStack(null)
                add(R.id.fragmentContainer, ChatFragment.newInstance(), "chat")
                addToBackStack(null)
                add(R.id.fragmentContainer, DiscoveryFragment.newInstance(), "discovery")
                addToBackStack(null)
                add(R.id.fragmentContainer, MatchingFragment.newInstance(MatchingFragment.MAIN, arrayListOf("~")), "matching")
                addToBackStack(null)
            }


        }

        binding.buttonLeft.setOnClickListener {

            if (discoveryMatchingValue == DISCOVERY_STYLE)
                startActivityForResult(Intent(this, ChooseStyleActivity::class.java).putExtra("requestCode", AGAIN), AGAIN)
            else if (discoveryMatchingValue == DISCOVERY_RESIDENCE)
                startActivityForResult(Intent(this,ChoosePlaceActivity::class.java).putExtra("requestCode", AGAIN), AGAIN)
            else if (discoveryMatchingValue == DISCOVERY_TRIP)
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode", AGAIN), AGAIN)

        }


        binding.buttonRight1.setOnClickListener {

            if (profileOnOrNot) {
                        startActivity(Intent(this, SettingActivity::class.java))
            } else {

            }
        }

        binding.buttonRight2.setOnClickListener { startActivity(Intent(this, FilterActivity::class.java)) }

        binding.bottomNavigationBar.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {


                when(item.itemId) {
                    R.id.page_home -> {

                        profileOnOrNot = false

                        binding.buttonLeft.visibility = View.GONE
                        binding.buttonRight1.visibility = View.GONE
                        binding.buttonRight1.setBackgroundResource(resources.getIdentifier("icon_notification", "drawable", packageName))

                        binding.buttonRight2.visibility = View.VISIBLE

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)



                            show(matchingFragment)
                            hide(discoveryFragment)
                            hide(chatFragment)
                            hide(profileFragment)

                            discoveryMatchingFragment?.let { hide(it) }
                        }

                    }

                    R.id.page_discovery -> {

                        profileOnOrNot = false

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)


                            if (discoveryMatchingFragment != null) {

                                binding.buttonLeft.visibility = View.VISIBLE
                                binding.buttonRight1.visibility = View.GONE
                                binding.buttonRight2.visibility = View.VISIBLE

                                show(discoveryMatchingFragment!!)
                                hide(discoveryFragment)

                            } else {

                                binding.buttonLeft.visibility = View.GONE
                                binding.buttonRight1.visibility = View.GONE
                                binding.buttonRight2.visibility = View.GONE

                                show(discoveryFragment)
                            }

                            hide(matchingFragment)
                            hide(chatFragment)
                            hide(profileFragment)

                        }
                    }

                    R.id.page_lounge -> {
                        Toast.makeText(this@MainScreenActivity, "!!!", Toast.LENGTH_SHORT).show()

                        profileOnOrNot = false

                        binding.buttonLeft.visibility = View.GONE
                        binding.buttonRight1.visibility = View.GONE
                        binding.buttonRight2.visibility = View.GONE
                    }

                    R.id.page_chat -> {

                        profileOnOrNot = false

                        binding.buttonLeft.visibility = View.GONE
                        binding.buttonRight1.visibility = View.GONE
                        binding.buttonRight2.visibility = View.GONE

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(chatFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(profileFragment)

                            discoveryMatchingFragment?.let { hide(it) }
                        }

                    }

                    R.id.page_profile -> {

                        profileOnOrNot = true

                        binding.buttonLeft.visibility = View.GONE
                        binding.buttonRight1.visibility = View.VISIBLE
                        binding.buttonRight1.setBackgroundResource(resources.getIdentifier("icon_more", "drawable", packageName))

                        binding.buttonRight2.visibility = View.GONE

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(profileFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(chatFragment)

                            discoveryMatchingFragment?.let { hide(it) }
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

        discoveryMatchingFragment = supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment?
        if (discoveryMatchingFragment == null)
            Log.d("fragment-check", "? : null")
        else
            Log.d("fragment-check", "? : on")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (
            (requestCode == DISCOVERY_STYLE && data != null) ||
            (requestCode == DISCOVERY_RESIDENCE && data != null) ||
            (requestCode == DISCOVERY_TRIP && data != null)
        ) {

            supportFragmentManager.commit {

                setReorderingAllowed(true)


                discoveryMatchingFragment = supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment?


                if (discoveryMatchingFragment == null) {

                    val arrayList = data.getStringArrayListExtra("condition")!!

                    var whichFragment = 0

                    if (requestCode == DISCOVERY_STYLE)
                        whichFragment = MatchingFragment.STYLE
                    else if (requestCode == DISCOVERY_RESIDENCE)
                        whichFragment = MatchingFragment.RESIDENCE
                    else if (requestCode == DISCOVERY_TRIP)
                        whichFragment = MatchingFragment.TRIP

                    add(R.id.fragmentContainer, MatchingFragment.newInstance(whichFragment, arrayList), "discoveryMatching")
                    addToBackStack(null)

                }


                binding.buttonLeft.visibility = View.VISIBLE
                binding.buttonRight1.visibility = View.GONE
                binding.buttonRight2.visibility = View.VISIBLE

                /**
                show(discoveryMatchingFragment!!) **/
                hide(matchingFragment)

                hide(discoveryFragment)

                hide(chatFragment)

                hide(profileFragment)

                discoveryMatchingValue = DISCOVERY_STYLE
            }


        } else if (resultCode == 1000) {

            supportFragmentManager.commit {

                setReorderingAllowed(true)

                remove(discoveryMatchingFragment!!)

                supportFragmentManager.popBackStack()

                binding.buttonLeft.visibility = View.GONE
                binding.buttonRight1.visibility = View.GONE
                binding.buttonRight2.visibility = View.GONE

                show(discoveryFragment)
            }
        }
    }

    override fun onBackPressed() {

        if (!appClose) {

            appClose = true
            Toast.makeText(this, "한 번 더 클릭하면 앱이 종료됩니다.",Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(1250)
                appClose = false
            }

        } else {
            
        }
    }

    companion object {

        const val DISCOVERY_STYLE = 500
        const val DISCOVERY_RESIDENCE = 501
        const val DISCOVERY_TRIP = 502

        const val AGAIN = 1000
    }
}