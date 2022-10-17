package com.versec.versecko.view.discovery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.R
import com.versec.versecko.databinding.FragmentDiscoveryBinding
import com.versec.versecko.view.ChoosePlaceActivity
import com.versec.versecko.view.ChooseStyleActivity
import com.versec.versecko.view.discovery.adapter.DiscoveryMainAdapter

class DiscoveryFragment : Fragment() {

    private lateinit var binding : FragmentDiscoveryBinding
    private lateinit var bannerList: MutableList<Banner>
    private lateinit var adapter : DiscoveryMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDiscoveryBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bannerList = mutableListOf()
        val titleList = resources.getStringArray(R.array.banner_title).toList()
        val imageList = resources.getStringArray(R.array.banner_image).toList()
        val contentList = resources.getStringArray(R.array.banner_content).toList()

        titleList.forEachIndexed { index, title ->
            bannerList.add(index, Banner(title, contentList.get(index), imageList.get(index)))
        }

        adapter = DiscoveryMainAdapter(requireActivity(), bannerList) {

            if (it.equals(titleList.get(0))) {

                requireActivity().startActivityForResult(Intent(requireActivity(), ChooseStyleActivity::class.java).putExtra("requestCode", DISCOVERY_STYLE), DISCOVERY_STYLE)

            } else if (it.equals(titleList.get(1))) {

                requireActivity().startActivityForResult(Intent(requireActivity(), ChoosePlaceActivity::class.java).putExtra("requestCode", DISCOVERY_RESIDENCE), DISCOVERY_RESIDENCE)


            } else if (it.equals(titleList.get(2))) {

                requireActivity().startActivityForResult(Intent(requireActivity(), ChoosePlaceActivity::class.java).putExtra("requestCode", DISCOVERY_TRIP), DISCOVERY_TRIP)

            }

        }

        binding.recyclerDiscovery.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerDiscovery.adapter = adapter

    }

    companion object {

        const val DISCOVERY_STYLE = 500
        const val DISCOVERY_RESIDENCE = 501
        const val DISCOVERY_TRIP = 502

        const val AGAIN = 1000

        @JvmStatic
        fun newInstance() =
            DiscoveryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}