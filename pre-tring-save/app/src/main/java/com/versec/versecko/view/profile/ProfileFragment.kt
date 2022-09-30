package com.versec.versecko.view.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.R
import com.versec.versecko.databinding.FragmentProfileBinding
import com.versec.versecko.view.profile.adapter.ViewPagerAdapter
import com.versec.versecko.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val profileViewModel : ProfileViewModel by viewModel<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding

    private lateinit var adapter: ViewPagerAdapter


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

        var uriList = mutableListOf<String>()

        adapter = ViewPagerAdapter(uriList)

        binding.viewpagerProfileImage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerProfileImage) {
            tab, position ->

        }.attach()

        binding.buttonEditProfile.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())

            builder.setItems(R.array.profile_edit, DialogInterface.OnClickListener { dialogInterface, index ->

                when(index) {
                    0 -> startActivity(Intent(requireActivity(), ProfileModifyActivity::class.java))
                    1 -> startActivity(Intent(requireActivity(), ImageModifyActivity::class.java))
                }

            })

            builder.create().show()

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