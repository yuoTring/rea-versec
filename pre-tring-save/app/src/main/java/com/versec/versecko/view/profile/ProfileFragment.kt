package com.versec.versecko.view.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.versec.versecko.R
import com.versec.versecko.databinding.FragmentProfileBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.profile.adapter.ViewPagerAdapter
import com.versec.versecko.view.signup.adapter.TagAdapter
import com.versec.versecko.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProfileFragment : Fragment() {

    private val profileViewModel : ProfileViewModel by viewModel<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding

    private lateinit var adapter: ViewPagerAdapter

    private lateinit var tripAdapter : TagAdapter
    private lateinit var styleAdapter : TagAdapter

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var uriList = mutableListOf<String>()

        adapter = ViewPagerAdapter(uriList)

        binding.viewpagerProfileImage.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerProfileImage) {
            tab, position ->

        }.attach()

        tripAdapter = TagAdapter(mutableListOf()) {

        }

        binding.recyclerTripWish.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        binding.recyclerTripWish.adapter = tripAdapter

        styleAdapter = TagAdapter(mutableListOf()) {

        }

        binding.recyclerTripStyle.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        binding.recyclerTripStyle.adapter = styleAdapter

        binding.buttonEditProfile.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())

            builder.setItems(R.array.profile_edit, DialogInterface.OnClickListener { dialogInterface, index ->

                when(index) {
                    0 -> startActivityForResult(Intent(requireActivity(), ProfileModifyActivity::class.java), EDIT)
                    1 -> startActivityForResult(Intent(requireActivity(), ImageModifyActivity::class.java), EDIT)
                }

            })

            builder.create().show()

        }


        profileViewModel.userLocal.observe(viewLifecycleOwner, Observer {

            Log.d("profile-check", it.toString())

            uriList.removeAll(uriList)

            it.uriMap.forEach { entry ->

                if (!entry.value.equals("null"))
                uriList.add(entry.value)
            }

            if (uriList.size == 0) {

                lifecycleScope.launch {


                    val response = profileViewModel.getOwnUser_Remote()

                    when(response) {

                        is Response.Success -> {

                            uriList.clear()

                            response.data!!.uriMap.forEach { entry ->

                                if (!entry.value.equals("null"))
                                    uriList.add(entry.value)
                            }

                            adapter.updateImages(uriList)
                            adapter.notifyDataSetChanged()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }

                    }

                }
            }

            adapter.updateImages(uriList)
            adapter.notifyDataSetChanged()

            val age = Calendar.getInstance().get(Calendar.YEAR) - it.birth.substring(0,4).toInt()

            binding.textNickAndAge.setText(it.nickName+", "+age)
            binding.textMannerScore.setText("매너 점수: "+it.mannerScore.toString())

            if (it.mainResidence.equals(it.subResidence))
                binding.textResidence.setText(it.mainResidence)
            else
                binding.textResidence.setText(it.mainResidence+" "+it.subResidence)

            tripAdapter.updateTagList(it.tripWish)
            tripAdapter.notifyDataSetChanged()

            styleAdapter.updateTagList(it.tripStyle)
            styleAdapter.notifyDataSetChanged()

            binding.textSelfIntroduction.setText(it.selfIntroduction)
            binding.textKnock.setText(it.knock.toString())
        } )


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT) {

            binding.progressBar.show()

            lifecycleScope.launch {

                val response = profileViewModel.getOwnUser_Remote()
                when(response) {
                    is Response.Success -> {


                        if (response.data != null) {

                            val insertResponse = profileViewModel.insertUser_Local(response.data)

                            when (insertResponse) {

                                is Response.Success -> {
                                    binding.progressBar.hide()
                                }
                                is Response.Error -> {
                                    binding.progressBar.hide()
                                }
                                else -> {

                                }
                            }

                        } else {
                            binding.progressBar.hide()
                        }


                    }
                    is Response.Error -> {
                        binding.progressBar.hide()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    companion object {

        private const val EDIT = 400

         @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}