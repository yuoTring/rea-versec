package com.versec.versecko.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

                                AppContext.uid = uid.data

                                startActivity(Intent(this@SplashActivity, ToMainScreenActivity::class.java))
                            }
                        }
                        is Response.No -> {

                            lifecycleScope.launch {
                                delay(2500)

                                startActivity(Intent(this@SplashActivity, EntryActivity::class.java))
                            }

                        }
                        is Response.Error -> {

                            lifecycleScope.launch {
                                delay(1000)


                                val builder = AlertDialog.Builder(this@SplashActivity)

                                builder.setTitle("~~~~~").create().show()



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