package com.versec.versecko.view.matching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityUserProfileBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.profile.adapter.ViewPagerAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.DetailProfileViewModel
import com.versec.versecko.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    companion object {
        val NOT_LOUNGE = 0
        val LIKED = 2
        val MATCHING =3
    }

    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel : DetailProfileViewModel by viewModel<DetailProfileViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var otherUser : UserEntity
    private lateinit var ownUser : UserEntity

    private lateinit var imageAdapter : ViewPagerAdapter

    private lateinit var tripAdapter : TagAdapter
    private lateinit var styleAdapter: TagAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        otherUser = intent.getSerializableExtra("otherUser") as UserEntity
        Log.d("error-check","other: "+otherUser.toString())


        mainViewModel.userLocal.observe(this, Observer {
            ownUser = it

            Log.d("error-check","own: "+ownUser.toString())
        })


        init()

    }

    fun init () {

        binding.buttonReject.setOnClickListener {
            binding.progressBarUserProfile.show()

            if (otherUser.loungeStatus == NOT_LOUNGE) {

                lifecycleScope.launch { viewModel.reject(otherUser) }
                binding.progressBarUserProfile.hide()
                setResult(1)
                finish()


            } else if (otherUser.loungeStatus == LIKED) {

                lifecycleScope.launch {

                    when(viewModel.rejectLiked(otherUser)) {
                        is Response.Success -> {
                            binding.progressBarUserProfile.hide()
                            finish()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }
                }

            } else if (otherUser.loungeStatus == MATCHING) {

                lifecycleScope.launch {

                    when(viewModel.rejectMatched(otherUser)) {
                        is Response.Success -> {
                            binding.progressBarUserProfile.hide()
                            finish()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }

                }
            }

        }

        binding.buttonAccept.setOnClickListener {

            binding.progressBarUserProfile.show()

            if (otherUser.loungeStatus == NOT_LOUNGE) {

                lifecycleScope.launch {
                    when(viewModel.like(otherUser, ownUser)) {
                        is Response.Success -> {
                            binding.progressBarUserProfile.hide()
                            setResult(1)
                            finish()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }

                }

            } else if (otherUser.loungeStatus == LIKED) {

                lifecycleScope.launch {

                    when(viewModel.match(otherUser, ownUser)) {
                        is Response.Success -> {
                            binding.progressBarUserProfile.hide()
                            finish()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    } }

            } else if (otherUser.loungeStatus == MATCHING) {

                lifecycleScope.launch {

                    val openResponse = viewModel.openChat(otherUser, ownUser)

                    when(openResponse) {
                        is Response.Success -> {

                            binding.progressBarUserProfile.hide()
                            Toast.makeText(this@UserProfileActivity, "채팅방이 열렸습니다!",Toast.LENGTH_SHORT).show()

                            when(viewModel.deleteMatch(otherUser.uid)) {
                                is Response.Success -> {
                                    finish()
                                }
                                is Response.Error -> {
                                }
                                else -> {

                                }
                            }

                        }
                        is Response.Error -> {
                            Log.d("error-check", openResponse.errorMessage)
                        }
                        else -> {

                        }
                    } }

            }
        }

        if (otherUser.loungeStatus == NOT_LOUNGE) { binding.buttonAccept.setImageResource(R.drawable.icon_heart) }
        else if (otherUser.loungeStatus == LIKED) { binding.buttonAccept.setImageResource(R.drawable.icon_heart) }
        else if (otherUser.loungeStatus == MATCHING ) { binding.buttonAccept.setImageResource(R.drawable.icon_chat) }

        val uriList = mutableListOf<String>()

        otherUser.uriMap.forEach { entry ->
            uriList.add(entry.key.toInt(), entry.value)
        }

        imageAdapter = ViewPagerAdapter(uriList)

        binding.viewpagerProfileImage.adapter = imageAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerProfileImage) { tab, position -> }

        val age = Calendar.getInstance().get(Calendar.YEAR) - otherUser.birth.substring(0,4).toInt()

        binding.textNickAndAge.setText(otherUser.nickName+" ,"+age)

        binding.textResidence.setText(otherUser.mainResidence+" "+otherUser.subResidence)
        binding.textMannerScore.setText(otherUser.mannerScore.toString())

        tripAdapter = TagAdapter(otherUser.tripWish) {

        }

        styleAdapter = TagAdapter(otherUser.tripStyle) {

        }

        binding.recyclerTripWish.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        binding.recyclerTripWish.adapter = tripAdapter

        binding.recyclerTripStyle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerTripStyle.adapter = styleAdapter

        binding.textSelfIntroduction.setText(otherUser.selfIntroduction)

    }
}