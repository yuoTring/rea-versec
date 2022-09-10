package com.versec.versecko.view.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentChatBinding
import com.versec.versecko.view.chat.adapter.LoungeAdapter
import com.versec.versecko.view.matching.UserProfileActivity
import com.versec.versecko.viewmodel.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatFragment : Fragment() {



    private lateinit var loungeUserList : MutableList<UserEntity>
    private lateinit var binding : FragmentChatBinding
    private val viewModel : ChatViewModel by viewModel<ChatViewModel>()

    private lateinit var loungeAdapter: LoungeAdapter
    //private lateinit var loungeLayoutManager: RecyclerView.LayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_chat,container,false)
        val view = binding.root

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loungeUserList = mutableListOf()


        viewModel.getLoungeUser(2)



        loungeAdapter = LoungeAdapter(loungeUserList) {

            var intent = Intent(requireActivity(), UserProfileActivity::class.java)
            intent.putExtra("user", it)
            startActivity(intent)
        }

        binding.recyclerLikeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerLikeList.adapter = loungeAdapter





        viewModel._loungeLikedUser.observe(viewLifecycleOwner, Observer {

            Log.d("user-lounge", it.size.toString()+"<- liked")
            loungeAdapter.updateUsers(it)
            loungeAdapter.notifyDataSetChanged()
        })

        viewModel._loungeMatchedUser.observe(viewLifecycleOwner, Observer {

            Log.d("user-lounge", it.size.toString()+"<- matched")
            loungeAdapter.updateUsers(it)
            loungeAdapter.notifyDataSetChanged()

        })


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ChatFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}