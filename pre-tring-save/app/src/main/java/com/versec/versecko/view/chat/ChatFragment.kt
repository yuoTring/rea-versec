package com.versec.versecko.view.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.versec.versecko.R
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentChatBinding
import com.versec.versecko.view.chat.adapter.LoungeAdapter
import com.versec.versecko.viewmodel.ChatViewModel


class ChatFragment : Fragment() {



    private lateinit var loungeUserList : MutableList<UserEntity>
    private lateinit var binding : FragmentChatBinding
    private val viewModel : ChatViewModel by viewModels<ChatViewModel>()

    private lateinit var loungeAdapter: LoungeAdapter
    private lateinit var loungeLayoutManager: RecyclerView.LayoutManager



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loungeUserList = mutableListOf()


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}