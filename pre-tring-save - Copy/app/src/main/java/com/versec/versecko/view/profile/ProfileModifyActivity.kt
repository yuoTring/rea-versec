package com.versec.versecko.view.profile

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityProfileModifyBinding
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.signup.FillUserInfoActivity
import com.versec.versecko.view.signup.adapter.ImageAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.ProfileModifyViewModel
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime

class ProfileModifyActivity : AppCompatActivity() {

    private lateinit var userEntity: UserEntity

    private val viewModel : ProfileModifyViewModel by viewModel<ProfileModifyViewModel>()

    private lateinit var binding : ActivityProfileModifyBinding
    private lateinit var view : View

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var emptyList: MutableList<String>
    private lateinit var imageList: MutableList<Uri>

    private var onClickImagePosition : Int = 0

    private lateinit var adapterResidence : TagAdapter
    private lateinit var adapterTrip : TagAdapter
    private lateinit var adapterStyle : TagAdapter

    private lateinit var residenceList : MutableList<String>
    private lateinit var tripList: MutableList<String>
    private lateinit var styleList: MutableList<String>

    companion object {

        const val RESIDENCE = 1
        const val TRIP =2

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityProfileModifyBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        emptyList = mutableListOf("empty","empty","empty","empty","empty","empty")
        imageList = mutableListOf(Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"),Uri.parse("---"))
        residenceList = mutableListOf("+")
        tripList = mutableListOf("+")
        styleList = mutableListOf("+")

        viewModel._user.observe(this, Observer { updatedUser ->

            userEntity = updatedUser



            residenceList.add(userEntity.mainResidence)
            tripList.addAll(userEntity.tripWish)
            styleList.addAll(userEntity.tripStyle)

            binding.editSelfIntroduction.setText(userEntity.selfIntroduction)


            userEntity.uriMap.forEach { entry ->

                imageList.set(entry.key.toInt(),Uri.parse(entry.value) )
                emptyList.set(entry.key.toInt(), "image")

            }


        })





        layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL, false)

        binding.buttonBack.setOnClickListener { finish() }

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)

        imageAdapter = ImageAdapter(emptyList, imageList, resourceId) { item, position ->

            // add a new image
            if (emptyList.get(position) == "empty") {

                onClickImagePosition = position
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)

            }
            // already image added
            else {

            }


        }

        binding.recyclerChosenImages.layoutManager = layoutManager
        binding.recyclerChosenImages.adapter = imageAdapter


        val layoutManagerResidence: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerTrip
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerStyle
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapterResidence = TagAdapter(residenceList) { place ->

            if (place.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.RESIDENCE
                ), FillUserInfoActivity.RESIDENCE
                )

            residenceList.removeAll(residenceList)
            residenceList.add("+")
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()



        }

        binding.recyclerChosenResidence.layoutManager = layoutManagerResidence
        binding.recyclerChosenResidence.adapter = adapterResidence

        adapterTrip = TagAdapter(tripList) { trip ->

            if (trip.equals("+"))
                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.TRIP
                ), FillUserInfoActivity.TRIP
                )
            else {
                tripList.remove(trip)
                adapterTrip.updateTagList(tripList)
                adapterTrip.notifyDataSetChanged()
            }

        }

        binding.recyclerChosenTripWish.layoutManager = layoutManagerTrip
        binding.recyclerChosenTripWish.adapter = adapterTrip

        binding.recyclerChosenStyle.layoutManager = layoutManagerStyle


        binding.buttonComplete.setOnClickListener {

            var checkImageReadyOrNot = false

            emptyList.forEach {
                if (it == "image")
                    checkImageReadyOrNot = true
            }

            /**
            imageList.forEachIndexed { index, uri ->

            if (it == Uri.parse("---"))

            }**/

            var count =0
            //var uriMap : MutableMap<Int, String> = mutableMapOf()
            var uriMap : MutableMap<String, Uri>
                    = mutableMapOf()

            emptyList.forEachIndexed { index, item ->

                if (item.equals("image")) {
                    uriMap.put(count.toString(), imageList.get(index))
                    count++
                }

            }

            if (checkImageReadyOrNot){

                Log.d("image-get", "uriMap: "+ uriMap.toString())

                //viewModel.uploadImage(uriMap)



            }
            else
            {
                Toast.makeText(this,"nonono", Toast.LENGTH_SHORT).show()
            }





        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            val uri : Uri? = data.data

            val uCrop = uri?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UCrop.of(it, Uri.fromFile(File(cacheDir, "cropped___"+ LocalDateTime.now().toString()+".png")))
                        .withAspectRatio(1.0F, 1.0F)
                        .start(this)
                }
            }

        }
        else if (requestCode ==100 && resultCode == RESULT_CANCELED) { }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {

            val croppedImageUri = UCrop.getOutput(data)

            if (croppedImageUri != null) {
                imageList.set(onClickImagePosition, croppedImageUri)
                emptyList.set(onClickImagePosition, "image")
                Log.d("image-get", "emptyList: "+ emptyList.toString())
                Log.d("image-get", "imageList: "+ imageList.toString())

                imageAdapter.updateList(emptyList, imageList)
                imageAdapter.notifyDataSetChanged()
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Log.d("error", "filluserimage-uploadimage-ucrop-error")

        }
        else if (requestCode == FillUserInfoActivity.RESIDENCE && data != null) {
            residenceList.removeAll(residenceList)
            residenceList.add("+")
            residenceList.addAll(data.getStringArrayListExtra("residence")!!)
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

            userEntity.mainResidence = residenceList.get(1)
        }
        else if (requestCode == FillUserInfoActivity.RESIDENCE && data == null) {

            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()

        }
        else if (requestCode == FillUserInfoActivity.TRIP && data != null) {

            tripList.removeAll(tripList)
            tripList.add("+")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()

            userEntity.tripWish.addAll(tripList)

        }
        else if (requestCode == FillUserInfoActivity.TRIP && data == null) {
            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()

        }
        else {
            Toast.makeText(this, "4", Toast.LENGTH_SHORT).show()

        }






    }
}