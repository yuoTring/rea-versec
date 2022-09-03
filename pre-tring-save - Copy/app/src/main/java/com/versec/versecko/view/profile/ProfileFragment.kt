package com.versec.versecko.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentProfileBinding
import com.versec.versecko.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val profileViewModel : ProfileViewModel by viewModel<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile,container,false)

        binding.viewModel = profileViewModel


        val view = binding.root



        return view
        //inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val observer_local = Observer<UserEntity> { updatedUser ->

            profileViewModel.userEntity= updatedUser
            //binding.textResidence.setText(updatedUser.mainResidence)
            //binding.textMannerScore.setText("매너 점수: "+updatedUser.mannerScore.toString())
            //binding.textNickAndAge.setText(updatedUser.nickName+", "+updatedUser.age)
            Log.d("profile-user", "local: "+updatedUser.toString())
            //binding.textNickAndAge.setText(updatedUser.nickName+", " +updatedUser.age)

        }

        profileViewModel._userLcoal.observe(viewLifecycleOwner, observer_local)

        val observer_remote = Observer<UserEntity> { updatedUser ->

            profileViewModel.userEntity = updatedUser
            Log.d("profile-user", "remote: "+updatedUser.toString())

            profileViewModel.insertUser_Local(updatedUser)

        }

        profileViewModel._userRemote.observe(viewLifecycleOwner, observer_remote)


        binding.buttonEditProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileModifyActivity::class.java))
        }


    }

    companion object {

         @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}