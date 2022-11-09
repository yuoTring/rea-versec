package com.versec.versecko.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.view.signup.ToMainScreenActivity
import com.versec.versecko.util.Response
import com.versec.versecko.view.signup.EntryActivity
import com.versec.versecko.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity()
{

    private val viewModel : SplashViewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        val uid = viewModel.getUid()

        when(uid) {

            is Response.Exist -> {


                lifecycleScope.launch {


                    val exist = viewModel.checkUid(uid.data!!)

                    when(exist) {
                        is Response.Exist -> {

                            lifecycleScope.launch {

                                delay(250)

                                AppContext.uid = uid.data
                                Log.d("uid-check", uid.data)

                                startActivity(Intent(this@SplashActivity, ToMainScreenActivity::class.java))
                            }
                        }
                        is Response.No -> {

                            lifecycleScope.launch {
                                delay(500)

                                startActivity(Intent(this@SplashActivity, EntryActivity::class.java))
                            }

                        }
                        is Response.Error -> {

                            lifecycleScope.launch {
                                delay(250)


                                val builder = AlertDialog.Builder(this@SplashActivity)

                                builder.setTitle("로그인 오류가 발생했습니다. 잠시 기다렸다가 다시 시도하십시오").create().show()



                            }

                        }
                        else -> {

                        }
                    }
                }



            }

            is Response.No -> {

                lifecycleScope.launch {

                    delay(2500)

                    startActivity(Intent(this@SplashActivity, EntryActivity::class.java))

                }
            }
            else -> {

            }
        }
    }

    override fun onBackPressed() {

    }
}