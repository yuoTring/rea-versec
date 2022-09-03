package com.versec.versecko.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityUserTestBinding
import com.versec.versecko.viewmodel.UserViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime

class UserTestActivity : AppCompatActivity()
{
    private val userViewModel : UserViewModel by viewModel<UserViewModel>()


    private lateinit var binding : ActivityUserTestBinding


    private var count : Int =0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_test)


        //userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)



        //binding = DataBindingUtil.setContentView(this, R.layout.activity_user_test)
        //binding.user = UserEntity()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_test)


        val userObserver = Observer<UserEntity> { newUserList ->

            Toast.makeText(this, "called: "+newUserList.toString(), Toast.LENGTH_SHORT).show()

            Log.d("room-db-status", "size: "+newUserList.toString())


        }

        userViewModel._user.observe(this, userObserver)



        binding.testButton.setOnClickListener(View.OnClickListener {


            count++
            Toast.makeText(this, "updated: "+count, Toast.LENGTH_SHORT).show()


            userViewModel.insertUser(UserEntity(
                uid = "testestestuiduiduid_____",
                nickName = "aeaaaaaa",
                gender ="female",
                age = 22,
                birth ="19990901",
                mainResidence= "Seoul",
                subResidence = "???",
                tripWish = mutableListOf("!!!","!!?"),
                tripStyle = mutableListOf("!!!","!!?"),
                selfIntroduction = "hi -_-",
                uriMap = mutableMapOf(),
                //uriList = mutableListOf("!!!","!!?"),
                geohash = "none",
                latitude = 37.455,
                longitude = 124.890,
                mannerScore = 4.5,
                premiumOrNot = false,
                knock = 0,
                loungeStatus = 0

            ))




        })

        binding.testImageUploadBtn.setOnClickListener {

            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),100)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==100 && resultCode== RESULT_OK && data != null)
        {
            Log.d("ucrop-test", "data: "+data.toString()+", data type: "+data.type)

            val uri : Uri? = data.data
            Log.d("ucrop-test", uri.toString())

            val uCrop = uri?.let { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                UCrop.of(it, Uri.fromFile(File(cacheDir, "cropped_"+LocalDateTime.now().toString()+".png")))
                    .withAspectRatio(1.0F, 1.0F)
                    .start(this@UserTestActivity)
            }
            }


        }
        else if (requestCode==100 &&resultCode == RESULT_CANCELED && data != null)
        {
            Log.d("ucrop-test", "!!!"+data.toString())

        }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null)
        {

            val croppedImageUri = UCrop.getOutput(data)

            if (croppedImageUri != null) {

                userViewModel.uploadImage(File(croppedImageUri.path))
            }



            /**
            if (croppedImageUri != null) {


                val file = compressImage(File(croppedImageUri.path))

                if (file != null) {
                    Log.d("file-size", "file-size-resized: "+ file.length()/1024)
                }

            }**/




        }
        else if (requestCode == UCrop.RESULT_ERROR)
        {
            Log.d("ucrop-test", "UCrop: Result-Error")
        }
        else
        {
            Log.d("ucrop-test", "else")
        }
    }

    fun compressImage(file: File) : File? {

        try {

            val options : BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = 6

            var inputStream = FileInputStream(file)

            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            val REQUIRED_SIZE = 67
            var scale =1
            //val scale : Int = 1

            while (options.outWidth/scale/2 >= REQUIRED_SIZE &&
                options.outHeight / scale / 2 >= REQUIRED_SIZE) {

                scale *= 2
            }

            val anotherOption : BitmapFactory.Options = BitmapFactory.Options()

            anotherOption.inSampleSize = scale

            inputStream = FileInputStream(file)

            val chosenBitmap =
                BitmapFactory.decodeStream(inputStream, null, anotherOption)

            inputStream.close()

            file.createNewFile()
            val outputStream = FileOutputStream(file)

            if (chosenBitmap != null) {
                chosenBitmap.compress(Bitmap.CompressFormat.PNG, 75, outputStream)
            }

            return file

        }
        catch (exception : Exception) {

            return null
        }



    }


}