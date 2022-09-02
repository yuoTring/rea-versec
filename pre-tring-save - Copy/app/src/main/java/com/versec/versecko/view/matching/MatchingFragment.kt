package com.versec.versecko.view.matching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentMatchingBinding
import com.versec.versecko.viewmodel.MatchingViewModel

class MatchingFragment : Fragment()
{
    private lateinit var binding : FragmentMatchingBinding
    private val viewModel : MatchingViewModel by viewModels<MatchingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_matching,container,false)
        val view = binding.root


        return view
        //inflater.inflate(R.layout.fragment_matching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsersWithGeoHash().observe(viewLifecycleOwner, Observer {
            fetchedUserList ->

        })

    }

    companion object
    {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MatchingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}