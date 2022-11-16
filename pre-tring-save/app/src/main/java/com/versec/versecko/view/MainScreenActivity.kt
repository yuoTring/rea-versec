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
import com.versec.versecko.view.room.RoomFragment
import com.versec.versecko.view.discovery.DiscoveryFragment
import com.versec.versecko.view.matching.MatchingFragment
import com.versec.versecko.view.profile.ProfileFragment
import com.versec.versecko.view.story.StoryFeedFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private lateinit var roomFragment : RoomFragment
    private lateinit var discoveryFragment: DiscoveryFragment
    private lateinit var storyFeedFragment: StoryFeedFragment

    private var discoveryMatchingFragment : MatchingFragment? = null
    private var discoveryMatchingValue = 0

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
                add(R.id.fragmentContainer, RoomFragment.newInstance(), "chat")
                addToBackStack(null)
                add(R.id.fragmentContainer, StoryFeedFragment.newInstance(), "story")
                addToBackStack(null)
                add(R.id.fragmentContainer, DiscoveryFragment.newInstance(), "discovery")
                addToBackStack(null)
                add(R.id.fragmentContainer, MatchingFragment.newInstance(MatchingFragment.MAIN, arrayListOf("~")), "matching")
                addToBackStack(null)
            }


        }

        binding.bottomNavigationBar.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {


                when(item.itemId) {
                    R.id.page_home -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)



                            show(matchingFragment)
                            hide(discoveryFragment)
                            hide(storyFeedFragment)
                            hide(roomFragment)
                            hide(profileFragment)

                            discoveryMatchingFragment?.let { hide(it) }
                        }

                    }

                    R.id.page_discovery -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)


                            if (discoveryMatchingFragment != null) {

                                show(discoveryMatchingFragment!!)
                                hide(discoveryFragment)

                            } else {

                                show(discoveryFragment)
                            }

                            hide(matchingFragment)
                            hide(storyFeedFragment)
                            hide(roomFragment)
                            hide(profileFragment)

                        }
                    }

                    R.id.page_lounge -> {

                        supportFragmentManager.commit {

                            setReorderingAllowed(true)

                            show(storyFeedFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(roomFragment)
                            hide(profileFragment)

                            discoveryMatchingFragment?.let { hide(it) }
                        }
                    }

                    R.id.page_chat -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(roomFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(storyFeedFragment)
                            hide(profileFragment)

                            discoveryMatchingFragment?.let { hide(it) }
                        }

                    }

                    R.id.page_profile -> {

                        supportFragmentManager.commit {
                            setReorderingAllowed(true)

                            show(profileFragment)
                            hide(matchingFragment)
                            hide(discoveryFragment)
                            hide(storyFeedFragment)
                            hide(roomFragment)

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
        roomFragment = supportFragmentManager.findFragmentByTag("chat") as RoomFragment
        discoveryFragment = supportFragmentManager.findFragmentByTag("discovery") as DiscoveryFragment
        storyFeedFragment = supportFragmentManager.findFragmentByTag("story") as StoryFeedFragment

        discoveryMatchingFragment = supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment?
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


                //discoveryMatchingFragment = supportFragmentManager.findFragmentByTag("discoveryMatching") as MatchingFragment?


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


                /**
                show(discoveryMatchingFragment!!) **/
                hide(matchingFragment)

                hide(discoveryFragment)

                hide(storyFeedFragment)

                hide(roomFragment)

                hide(profileFragment)

            }
        }
        else if (
            (requestCode == AGAIN_STYLE && resultCode == CANCEL) ||
            (requestCode == AGAIN_RESIDENCE && resultCode == CANCEL) ||
            (requestCode == AGAIN_TRIP && resultCode == CANCEL)
        ) {

            supportFragmentManager.commit {

                setReorderingAllowed(true)

                remove(discoveryMatchingFragment!!)

                supportFragmentManager.popBackStack()

                show(discoveryFragment)
                hide(matchingFragment)
                hide(storyFeedFragment)
                hide(roomFragment)
                hide(profileFragment)

            }
        } else if (

            (requestCode == AGAIN_STYLE && data != null) ||
            (requestCode == AGAIN_RESIDENCE && data != null) ||
            (requestCode == AGAIN_TRIP && data != null)

        ) {

            Log.d("re-con", requestCode.toString())


            supportFragmentManager.commit {

                setReorderingAllowed(true)

                remove(discoveryMatchingFragment!!)

                supportFragmentManager.popBackStack()

                val arrayList = data.getStringArrayListExtra("condition")

                Log.d("re-con", arrayList.toString())


                var whichFragment = 0

                if (requestCode == AGAIN_STYLE)
                    whichFragment = MatchingFragment.STYLE
                else if (requestCode == AGAIN_RESIDENCE)
                    whichFragment = MatchingFragment.RESIDENCE
                else if (requestCode == AGAIN_TRIP)
                    whichFragment = MatchingFragment.TRIP

                add(R.id.fragmentContainer, MatchingFragment.newInstance(whichFragment, arrayList!!), "discoveryMatching")
                addToBackStack(null)

                hide(matchingFragment)

                hide(discoveryFragment)

                hide(storyFeedFragment)

                hide(roomFragment)

                hide(profileFragment)

                show(discoveryMatchingFragment!!)

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

        const val AGAIN_STYLE = 1000
        const val AGAIN_RESIDENCE = 1001
        const val AGAIN_TRIP = 1002

        private const val CANCEL = -1000
    }
}