package com.versec.versecko.view.profile

import com.versec.versecko.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.versec.versecko.databinding.ActivityImageModifyBinding
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.viewmodel.ImageModifyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageModifyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImageModifyBinding
    private val viewModel : ImageModifyViewModel by viewModel<ImageModifyViewModel>()

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList : MutableList<Uri>


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityImageModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageList = mutableListOf(Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"))

        binding.buttonBack.setOnClickListener { finish() }

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)

        imageAdapter = ImageAdapter(imageList, resourceId) { item, position ->


            val builder = AlertDialog.Builder(this)

            if (position == 0) {


                builder.setItems(
                    arrayOf("edit"),
                    DialogInterface.OnClickListener { dialogInterface, index ->



                })

                builder.create().show()



            } else {


                if (!imageList.get(position).toString().equals("---")) {

                    builder.setItems(
                        arrayOf("delete"),
                        DialogInterface.OnClickListener { dialogInterface, index ->




                        })

                    builder.create().show()

                }

                else {



                }



            }

        }

        viewModel.ownUser.observe(this, Observer {

            it.uriMap.forEach { entry ->

                imageList.set(entry.key.toInt(), Uri.parse(entry.value))

            }
        })



    }
}