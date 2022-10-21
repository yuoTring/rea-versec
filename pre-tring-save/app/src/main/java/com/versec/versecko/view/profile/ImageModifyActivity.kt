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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.versec.versecko.databinding.ActivityImageModifyBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.viewmodel.ImageModifyViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
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











        imageList = mutableListOf(Uri.parse("null"),Uri.parse("null"),Uri.parse("null"),Uri.parse("null"),Uri.parse("null"),Uri.parse("null"))

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)

        imageAdapter = ImageAdapter(imageList, resourceId, true) { item, position ->

            onClickImageIndex = position

            val builder = AlertDialog.Builder(this)

            if (position == 0) {


                builder.setItems(
                    arrayOf("수정"),
                    DialogInterface.OnClickListener { dialogInterface, index ->


                        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)

                })

                builder.create().show()



            } else {


                if (!imageList.get(position).toString().equals("null")) {

                    builder.setItems(
                        arrayOf("삭제"),
                        DialogInterface.OnClickListener { dialogInterface, index ->

                            binding.progressBar.show()

                            lifecycleScope.launch {

                                val response = viewModel.deleteImage(position)

                                when(response) {
                                    is Response.Success -> {

                                        binding.progressBar.hide()
                                        imageList.set(position, Uri.parse("null"))
                                        imageAdapter.updateList(imageList)
                                        imageAdapter.notifyDataSetChanged()

                                        syncOwnUser()
                                    }
                                    is Response.Error -> {

                                        binding.progressBar.hide()
                                        Toast.makeText(this@ImageModifyActivity, "인터넷 연결 오류로 인해 이미지가 삭제되지 않았습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {

                                    }
                                }

                            }


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

    private suspend fun syncOwnUser () {

        val remoteResponse = viewModel.getOwnUser_Remote()

        when (remoteResponse) {
            is Response.Success -> {

                if (remoteResponse.data != null) {

                    val localResponse = viewModel.insertUser_Local(remoteResponse.data)

                    when(localResponse) {

                        is Response.Success -> {

                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }


                } else {

                }
            }
            is Response.Error -> {

            }
            else -> {

            }
        }
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

                binding.progressBar.show()

                lifecycleScope.launch {

                    val response = viewModel.reuploadImage(onClickImageIndex, croppedImageUri)

                    when(response) {
                        is Response.Success -> {
                            binding.progressBar.hide()

                            imageList.set(onClickImageIndex, croppedImageUri)

                            imageAdapter.updateList(imageList)
                            imageAdapter.notifyDataSetChanged()

                            syncOwnUser()
                        }
                        is Response.Error -> {
                            binding.progressBar.hide()
                            Log.d("reupload-image-check", response.errorMessage)
                            Toast.makeText(this@ImageModifyActivity, "인터넷 연결 오류로 인해 이미지가 업로드되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {

                        }
                    }
                }
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Log.d("error", "filluserimage-uploadimage-ucrop-error")

        }





    }
}