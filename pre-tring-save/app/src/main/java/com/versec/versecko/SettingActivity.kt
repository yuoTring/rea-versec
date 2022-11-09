package com.versec.versecko

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.versec.versecko.databinding.*
import com.versec.versecko.util.Response
import com.versec.versecko.view.signup.SignInActivity
import com.versec.versecko.viewmodel.SettingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding
    private val viewModel : SettingViewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.user.observe(this, Observer {

            if (it.deletedAt == null) {

                binding.textActivate.visibility = View.INVISIBLE
                binding.textInactivate.visibility = View.VISIBLE

            } else {

                binding.textActivate.visibility = View.VISIBLE
                binding.textInactivate.visibility = View.INVISIBLE
            }

        } )

        init()
    }

    private fun init () {

        binding.buttonBack.setOnClickListener { finish() }

        if (viewModel.getMatchingSetting()) {
            binding.switchMatching.isChecked = true
        } else {
            binding.switchMatching.isChecked = false
        }

        binding.switchMatching.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                viewModel.setMatching(true)
            } else {
                viewModel.setMatching(false)
            }
        }

        if (viewModel.getLikedSetting()) {
            binding.switchLiked.isChecked = true
        } else {
            binding.switchLiked.isChecked = false
        }

        binding.switchLiked.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                viewModel.setLiked(true)
            } else {
                viewModel.setLiked(false)
            }
        }

        if (viewModel.getMessageSetting()) {
            binding.switchMessage.isChecked = true
        } else {
            binding.switchMessage.isChecked = false
        }

        binding.switchMessage.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                viewModel.setMessage(true)
            } else {
                viewModel.setMessage(false)
            }
        }

        if (viewModel.getMarketingNotificationStatus()) {
            binding.switchMarketing.isChecked = true
        } else {
            binding.switchMarketing.isChecked = false
        }

        binding.switchMarketing.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                viewModel.setMarketing(true)
            } else {
                viewModel.setMarketing(false)
            }
        }


        binding.textToService.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/ver-1-0-0-c7ff915f2e4946ba82ec99241baebb51"))) }
        binding.textToPI.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/Ver-1-0-0-fc7683ead4c04cbd96060dfecf8769e7"))) }
        binding.textToLI.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://coursedesign.notion.site/Ver-1-0-6dda9ec63e5748bd801a8ccb73948d4e"))) }

        binding.textLogOut.setOnClickListener {

            val builder = AlertDialog.Builder(this).create()

            val view = layoutInflater.inflate(R.layout.item_alert_logout,null)

            builder.setView(view)
            builder.show()

            ItemAlertLogoutBinding.bind(view).buttonConfirm.setOnClickListener {

                val response = viewModel.logOut()

                builder.dismiss()

                when(response) {
                    is Response.Success -> {
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    is Response.Error -> {
                        Toast.makeText(this,"로그아웃에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            }

            ItemAlertLogoutBinding.bind(view).buttonCancel.setOnClickListener {
                builder.dismiss()
            }
        }

        binding.textInactivate.setOnClickListener {

            val builder = AlertDialog.Builder(this).create()

            val view = layoutInflater.inflate(R.layout.item_alert_inactivate, null)

            builder.setView(view)
            builder.show()

            ItemAlertInactivateBinding.bind(view).buttonConfirm.setOnClickListener {

                binding.progressBar.show()

                lifecycleScope.launch {

                    val response = viewModel.inactivateAccount()

                    when(response) {
                        is Response.Success -> {


                            val updateResponse = viewModel.updateDeletedAt(false)

                            when(updateResponse) {
                                is Response.Success -> {

                                    binding.progressBar.hide()
                                    builder.dismiss()
                                }
                                is Response.Error -> {

                                    binding.progressBar.hide()
                                    builder.dismiss()
                                    Toast.makeText(this@SettingActivity,"계정 비활성화에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()
                                }
                                else -> {

                                }
                            }




                        }
                        is Response.Error -> {

                            binding.progressBar.hide()
                            builder.dismiss()
                            Toast.makeText(this@SettingActivity,"계정 비활성화에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()

                        }
                        else -> {

                        }
                    }
                }

            }

            ItemAlertInactivateBinding.bind(view).buttonCancel.setOnClickListener { builder.dismiss() }
        }

        binding.textActivate.setOnClickListener {

            val builder = AlertDialog.Builder(this).create()

            val view = layoutInflater.inflate(R.layout.item_alert_activate, null)

            builder.setView(view)
            builder.show()

            ItemAlertActivateBinding.bind(view).buttonConfirm.setOnClickListener {

                binding.progressBar.show()

                lifecycleScope.launch {

                    val response = viewModel.activate()

                    when(response) {
                        is Response.Success -> {

                            val updateResponse = viewModel.updateDeletedAt(true)

                            when(updateResponse) {
                                is Response.Success -> {

                                    binding.progressBar.hide()
                                    builder.dismiss()

                                }
                                is Response.Error -> {

                                    binding.progressBar.hide()
                                    builder.dismiss()
                                    Toast.makeText(this@SettingActivity,"계정 활성화에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()

                                }
                                else -> {

                                }
                            }




                        }
                        is Response.Error -> {

                            binding.progressBar.hide()
                            builder.dismiss()
                            Toast.makeText(this@SettingActivity,"계정 활성화에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()

                        }
                        else -> {

                        }
                    }
                } }
            ItemAlertActivateBinding.bind(view).buttonCancel.setOnClickListener { builder.dismiss() }
        }

        binding.textDelete.setOnClickListener {

            val builder = AlertDialog.Builder(this).create()

            val view = layoutInflater.inflate(R.layout.item_alert_delete, null)

            builder.setView(view)
            builder.show()

            ItemAlertDeleteBinding.bind(view).buttonConfirm.setOnClickListener {

                binding.progressBar.show()

                lifecycleScope.launch {

                    val response = viewModel.deleteAccount()

                    when(response) {
                        is Response.Success -> {

                            binding.progressBar.hide()
                            builder.dismiss()

                            startActivity(Intent(this@SettingActivity, SignInActivity::class.java))
                        }
                        is Response.Error -> {

                            binding.progressBar.hide()
                            builder.dismiss()

                            Toast.makeText(this@SettingActivity,"계정 삭제에 실패했습니다. 잠시 기다렸다가 다시 시도하십시오.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {

                        }
                    }
                }

            }

            ItemAlertDeleteBinding.bind(view).buttonCancel.setOnClickListener { builder.dismiss() }

        }

    }
}