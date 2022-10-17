package com.versec.versecko.view.signup

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
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityFillUserImageBinding
import com.versec.versecko.util.Response
import com.versec.versecko.util.WindowEventManager
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.viewmodel.FillUserImageViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random

class FillUserImageActivity : AppCompatActivity()
{
    private lateinit var userEntity: UserEntity

    private lateinit var binding : ActivityFillUserImageBinding
    private lateinit var view : View

    private val viewModel : FillUserImageViewModel by viewModel<FillUserImageViewModel>()

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var imageList: MutableList<Uri>

    private var onClickImagePosition : Int = 0
    private var imageCount : Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        var intent = intent
        userEntity= intent.getSerializableExtra("user") as UserEntity
        userEntity.uriMap = mutableMapOf()

        binding = ActivityFillUserImageBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        imageList = mutableListOf(Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"))

        layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL, false)

        binding.buttonBack.setOnClickListener { finish() }

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)

        imageAdapter = ImageAdapter(imageList, resourceId, false) { item, position ->

            // add a new image
            if (imageList.get(position).toString().equals("---")) {


                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)

            }
            // already image added
            else {

                val builder = AlertDialog.Builder(this)

                builder.setItems(arrayOf("delete"), DialogInterface.OnClickListener { dialogInterface, index ->

                    imageList.set(position, Uri.parse("---"))
                    imageCount --

                    imageList.removeAt(position)
                    imageList.add(Uri.parse("---"))


                    imageAdapter.updateList(imageList)
                    imageAdapter.notifyDataSetChanged()

                })

                builder.create().show()


            }


        }

        binding.recyclerChosenImages.layoutManager = layoutManager
        binding.recyclerChosenImages.adapter = imageAdapter



        binding.editSelfIntroduction.doAfterTextChanged {
            text ->

            var textCount = "0/200"

            if (text.toString().length>20) {

            }
            else
            {

                textCount=
                text.toString().length.toString() + "/200"
                binding.textCheckLength.setText(textCount)
            }


        }








        binding.buttonComplete.setOnClickListener {



            var checkImageReadyOrNot = false

            if (!imageList.get(0).toString().equals("---"))
                checkImageReadyOrNot = true


            if (checkImageReadyOrNot){

                for (x in 1000 .. 1000) {


                    val la = Random.nextDouble(-0.05, 0.05)
                    val lo = Random.nextDouble(-0.05,0.05)

                    userEntity.uid = AppContext.uid
                    //userEntity.nickName = "test__"+x
                    userEntity.selfIntroduction = binding.editSelfIntroduction.text.toString()

                    userEntity.latitude = userEntity.latitude + la
                    userEntity.longitude = userEntity.longitude + lo

                    userEntity.phoneNumber = "010-0000-"+x


                    val uriMap : MutableMap<String, Uri>
                            = mutableMapOf()


                    imageList.forEachIndexed { index, uri ->

                        if (!uri.toString().equals("---"))
                            uriMap.put(index.toString(), imageList.get(index))
                    }


                    lifecycleScope.launch {

                        show()

                        val insertResponse_Remote = viewModel.insertUser_Remote(userEntity)

                        val insertResponse_Local = viewModel.insertUser_Local(userEntity)



                        when(insertResponse_Remote) {
                            is Response.Success -> {

                                val uploadResult = viewModel.uploadImage(uriMap)

                                when(uploadResult) {
                                    is Response.Success -> {


                                        val fcmResponse = viewModel.saveFCMToken()

                                        when (fcmResponse) {
                                            is Response.Success -> {

                                            }
                                            is Response.Error -> {

                                                Log.d("error-!!!", fcmResponse.errorMessage)

                                            }
                                            else -> {

                                            }
                                        }

                                        val insertResponse_LocalWithUriMap = viewModel.getOwnUser()

                                        when(insertResponse_LocalWithUriMap) {
                                            is Response.Success -> {

                                                hide()
                                                startActivity(Intent(this@FillUserImageActivity, CongratsActivity::class.java))
                                            }
                                            is Response.Error -> {
                                                show()
                                            }
                                            else -> {

                                            }
                                        }



                                    }

                                    is Response.Error -> {


                                        hide()
                                    }
                                    else -> {

                                    }
                                }

                            }
                            is Response.Error -> {
                                hide()


                            }
                            else -> {

                            }
                        }

                        when(insertResponse_Local) {
                            is Response.Error -> {
                                hide()
                            }
                            else -> {

                            }
                        }
                    }

                }






            }
            else
            {
                Toast.makeText(this,"사진을 하나 이상 업로드하십시오.", Toast.LENGTH_SHORT).show()
            }





        }

    }

    private fun show () {
        binding.progressBar.show()
        WindowEventManager.blockUserInteraction(this@FillUserImageActivity)
    }

    private fun hide () {
        binding.progressBar.hide()
        WindowEventManager.openUserInteraction(this@FillUserImageActivity)
    }

    private fun insertUser () {


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
                imageList.set(imageCount, croppedImageUri)
                imageCount++

                Log.d("image-get", "imageList: "+ imageList.toString())

                imageAdapter.updateList(imageList)
                imageAdapter.notifyDataSetChanged()
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Log.d("error", "filluserimage-uploadimage-ucrop-error")

        }
    }



}