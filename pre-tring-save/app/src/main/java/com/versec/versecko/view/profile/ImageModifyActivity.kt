package com.versec.versecko.view.profile

import com.versec.versecko.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.versec.versecko.databinding.ActivityImageModifyBinding
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.viewmodel.ImageModifyViewModel
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime

class ImageModifyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImageModifyBinding
    private val viewModel : ImageModifyViewModel by viewModel<ImageModifyViewModel>()

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList : MutableList<Uri>

    private var onClickImageIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityImageModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)











        imageList = mutableListOf(Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"))

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)

        imageAdapter = ImageAdapter(imageList, resourceId, true) { item, position ->

            onClickImageIndex = position

            val builder = AlertDialog.Builder(this)

            if (position == 0) {


                builder.setItems(
                    arrayOf("edit"),
                    DialogInterface.OnClickListener { dialogInterface, index ->


                        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)

                })

                builder.create().show()



            } else {


                if (!imageList.get(position).toString().equals("---")) {

                    builder.setItems(
                        arrayOf("delete"),
                        DialogInterface.OnClickListener { dialogInterface, index ->

                            viewModel.deleteImage(position)
                            imageList.set(position, Uri.parse("---"))
                            imageAdapter.updateList(imageList)
                            imageAdapter.notifyDataSetChanged()

                        })

                    builder.create().show()

                }

                else {

                    startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)

                }



            }

        }

        binding.recyclerChosenImages.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerChosenImages.adapter = imageAdapter


        viewModel.ownUser.observe(this, Observer {

            it.uriMap.forEach { entry ->

                imageList.set(entry.key.toInt(), Uri.parse(entry.value))

            }

            imageAdapter.updateList(imageList)
            imageAdapter.notifyDataSetChanged()
        })


        binding.buttonBack.setOnClickListener { finish() }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            val uri : Uri? = data.data

            val uCrop = uri?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UCrop.of(it, Uri.fromFile(File(cacheDir, "cropped___"+LocalDateTime.now().toString()+".png")))
                        .withAspectRatio(1.0F, 1.0F)
                        .start(this)
                }
            }

        }
        else if (requestCode ==100 && resultCode == RESULT_CANCELED) { }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {

            val croppedImageUri = UCrop.getOutput(data)

            if (croppedImageUri != null) {

                imageList.set(onClickImageIndex, croppedImageUri)

                imageAdapter.updateList(imageList)
                imageAdapter.notifyDataSetChanged()

                viewModel.uploadImage(onClickImageIndex, croppedImageUri)
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Log.d("error", "filluserimage-uploadimage-ucrop-error")

        }





    }
}