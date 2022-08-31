package com.versec.versecko.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserTestActivity : AppCompatActivity()
{
    private val userViewModel : UserViewModel by viewModel<UserViewModel>()

    private lateinit var  button : AppCompatButton

    //lateinit var binding: ActivityUserTestBinding

    private var count : Int =0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_test)


        //userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        button = findViewById(R.id.testButton)

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_user_test)
        //binding.user = UserEntity()


        val userObserver = Observer<UserEntity> { newUserList ->

            Toast.makeText(this, "called: "+newUserList.toString(), Toast.LENGTH_SHORT).show()

            Log.d("room-db-status", "size: "+newUserList.toString())


        }

        userViewModel._user.observe(this, userObserver)



        button.setOnClickListener(View.OnClickListener {


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
                uriList = mutableListOf("!!!","!!?"),
                geohash = "none",
                latitude = 37.455,
                longitude = 124.890,
                mannerScore = 4.5,
                premiumOrNot = false,
                knock = 0,
                loungeStatus = 0

            ))




        })
    }


}