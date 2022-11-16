package com.versec.versecko.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.ActivityProfileModifyBinding
import com.versec.versecko.util.Response
import com.versec.versecko.util.WindowEventManager
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.ChooseStyleActivity
import com.versec.versecko.view.signup.FillUserInfoActivity
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.ProfileModifyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileModifyActivity : AppCompatActivity() {

    private lateinit var userEntity: UserEntity

    private val viewModel : ProfileModifyViewModel by viewModel<ProfileModifyViewModel>()

    private lateinit var binding : ActivityProfileModifyBinding
    private lateinit var view : View

    private lateinit var layoutManager: RecyclerView.LayoutManager

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


        residenceList = mutableListOf("다시 설정")
        tripList = mutableListOf("다시 설정")
        styleList = mutableListOf("다시 설정")


        viewModel.user.observe(this, Observer { updatedUser ->

            userEntity = updatedUser



            residenceList.add(userEntity.mainResidence)
            residenceList.add(userEntity.subResidence)

            tripList.addAll(userEntity.tripWish)
            styleList.addAll(userEntity.tripStyle)

            adapterResidence.updateTagList(residenceList)
            adapterTrip.updateTagList(tripList)
            adapterStyle.updateTagList(styleList)

            adapterResidence.notifyDataSetChanged()
            adapterTrip.notifyDataSetChanged()
            adapterStyle.notifyDataSetChanged()

            binding.editSelfIntroduction.setText(userEntity.selfIntroduction)
        })






        layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL, false)

        binding.buttonBack.setOnClickListener { finish() }

        val resourceId = resources.getIdentifier("button_add","drawable", packageName)




        val layoutManagerResidence: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerTrip
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerStyle
                : RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)

        adapterResidence = TagAdapter(residenceList) { place ->

            if (place.equals("다시 설정")) {

                residenceList.clear()
                residenceList.add("다시 설정")

                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.RESIDENCE
                ), FillUserInfoActivity.RESIDENCE
                )

            } else {

                residenceList.remove(place)

            }

            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()




        }

        binding.editSelfIntroduction.doAfterTextChanged {
                text ->

            var textCount = "0/200"

            if (text.toString().length>200) {

                binding.editSelfIntroduction.text.delete(text!!.length-1, text.length)
            }
            else
            {

                textCount=
                    text.toString().length.toString() + "/200"
                binding.textCheckLength.setText(textCount)
            }


        }

        binding.recyclerChosenResidence.layoutManager = layoutManagerResidence
        binding.recyclerChosenResidence.adapter = adapterResidence

        adapterTrip = TagAdapter(tripList) { trip ->

            if (trip.equals("다시 설정")) {

                tripList.clear()
                tripList.add("다시 설정")

                startActivityForResult(Intent(this, ChoosePlaceActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.TRIP
                ), FillUserInfoActivity.TRIP
                )
            }
            else {
                tripList.remove(trip)

            }

            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()

        }

        binding.recyclerChosenTripWish.layoutManager = layoutManagerTrip
        binding.recyclerChosenTripWish.adapter = adapterTrip

        adapterStyle = TagAdapter(styleList) { style ->

            if (style.equals("다시 설정")) {

                styleList.clear()
                styleList.add("다시 설정")

                startActivityForResult(Intent(this, ChooseStyleActivity::class.java).putExtra("requestCode",
                    FillUserInfoActivity.STYLE
                ), FillUserInfoActivity.STYLE
                )

            } else {

                styleList.remove(style)

            }

            adapterStyle.updateTagList(styleList)
            adapterStyle.notifyDataSetChanged()


        }

        binding.recyclerChosenStyle.layoutManager = layoutManagerStyle
        binding.recyclerChosenStyle.adapter = adapterStyle


        binding.buttonComplete.setOnClickListener {

            userEntity.mainResidence = residenceList.get(1)

            if (residenceList.size > 2)
                userEntity.subResidence = residenceList.get(2)
            else
                userEntity.subResidence = residenceList.get(1)

            if (tripList.contains("다시 설정"))
                tripList.remove("다시 설정")

            if (styleList.contains("다시 설정"))
                styleList.remove("다시 설정")


            userEntity.tripWish = tripList
            userEntity.tripStyle = styleList

            userEntity.selfIntroduction = binding.editSelfIntroduction.text.toString()

            lifecycleScope.launch {

                show()

                val insertResponse_Remote = viewModel.insertUser_Remote(userEntity)

                when(insertResponse_Remote) {

                    is Response.Success -> {


                        val insertResponse_Local = viewModel.insertUser_Local(userEntity)

                        when(insertResponse_Local) {

                            is Response.Success -> {

                                hide()
                                finish()
                            }
                            is Response.Error -> {

                                hide()
                                finish()
                                Toast.makeText(this@ProfileModifyActivity , "인터넷 연결 오류로 인해 프로필이 수정되지 않았습니다.", Toast.LENGTH_LONG).show()

                            }
                            else -> {

                            }
                        }




                    }
                    is Response.Error -> {

                        hide()
                        finish()
                        Toast.makeText(this@ProfileModifyActivity , "인터넷 연결 오류로 인해 프로필이 수정되지 않았습니다.", Toast.LENGTH_LONG).show()
                    }
                    else -> {

                    }
                }





            }
        }


    }

    private fun show() {

        binding.progressBar.show()
        WindowEventManager.blockUserInteraction(this)
    }

    private fun hide() {

        binding.progressBar.hide()
        WindowEventManager.openUserInteraction(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == FillUserInfoActivity.RESIDENCE && data != null) {
            residenceList.clear()
            residenceList.add("다시 설정")
            residenceList.addAll(data.getStringArrayListExtra("residence")!!)
            adapterResidence.updateTagList(residenceList)
            adapterResidence.notifyDataSetChanged()

            userEntity.mainResidence = residenceList.get(1)

            if (residenceList.size>2)
                userEntity.subResidence = residenceList.get(2)
        }
        else if (requestCode == FillUserInfoActivity.RESIDENCE && data == null) {


        }
        else if (requestCode == FillUserInfoActivity.TRIP && data != null) {

            tripList.clear()
            tripList.add("다시 설정")
            tripList.addAll(data.getStringArrayListExtra("trip")!!)
            adapterTrip.updateTagList(tripList)
            adapterTrip.notifyDataSetChanged()

            userEntity.tripWish.clear()
            userEntity.tripWish.addAll(tripList)

        }
        else if (requestCode == FillUserInfoActivity.TRIP && data == null) {

            //tripList.add("다시 설정")
            //adapterTrip.updateTagList(tripList)
            //adapterTrip.notifyDataSetChanged()

        }
        else if (requestCode == FillUserInfoActivity.STYLE && data != null) {

            styleList.clear()
            styleList.add("다시 설정")
            styleList.addAll(data.getStringArrayListExtra("style")!!)
            adapterStyle.updateTagList(styleList)
            adapterStyle.notifyDataSetChanged()

            userEntity.tripStyle.clear()
            userEntity.tripStyle.addAll(styleList)

        }






    }
}