package com.versec.versecko.view.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.versec.versecko.R
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentChatBinding
import com.versec.versecko.view.chat.adapter.ChatRoomAdapter
import com.versec.versecko.view.chat.adapter.LoungeAdapter
import com.versec.versecko.view.matching.UserProfileActivity
import com.versec.versecko.viewmodel.ChatViewModel
import com.versec.versecko.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatFragment : Fragment() {



    private lateinit var loungeUserList : MutableList<UserEntity>
    private lateinit var binding : FragmentChatBinding
    private val viewModel : ChatViewModel by viewModel<ChatViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var loungeAdapter: LoungeAdapter
    private var viewLoungeStatus : Int = 2
    private var usersLiked : MutableList<UserEntity> = mutableListOf()
    private var usersMatched : MutableList<UserEntity> = mutableListOf()


    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private var roomList : MutableList<ChatRoomEntity> = mutableListOf()

    private lateinit var ownUser : UserEntity



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


        binding.buttonTest.setOnClickListener {

            var ownUser = UserEntity()
            ownUser.uid = "test!!!!!"
            ownUser.uriMap = mutableMapOf("0" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=fd73fe45-808f-4540-b3ce-0ba5020a1c25")

            var other = UserEntity()
            other.uid = "otherUid"
            other.uriMap = mutableMapOf("0" to "https://firebasestorage.googleapis.com/v0/b/tring-2c450.appspot.com/o/image%2FprofileImages%2Ftest!!!!!%2Ftest!!!!!_0?alt=media&token=fd73fe45-808f-4540-b3ce-0ba5020a1c25")
            viewModel.openRoom(other,ownUser)
        }

        loungeUserList = mutableListOf()

        loungeAdapter = LoungeAdapter(loungeUserList) {

            var intent = Intent(requireActivity(), UserProfileActivity::class.java)
            intent.putExtra("user", it)
            startActivity(intent)
        }

        binding.recyclerLikeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerLikeList.adapter = loungeAdapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab != null) {

                    if (tab.position == 0) {

                        viewLoungeStatus = 2

                        binding.tabLayout.getTabAt(0)?.orCreateBadge!!.number

                        loungeAdapter.changeUsers(usersLiked)
                        loungeAdapter.notifyDataSetChanged()

                    } else {
                        viewLoungeStatus = 3

                        loungeAdapter.changeUsers(usersMatched)
                        loungeAdapter.notifyDataSetChanged()

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


        })




        viewModel.usersLiked.observe(viewLifecycleOwner, Observer {

            usersLiked.addAll(it)

            if (viewLoungeStatus == 2) {

                loungeAdapter.changeUsers(usersLiked)
                loungeAdapter.notifyDataSetChanged()
            }

        })

        viewModel.usersMatched.observe(viewLifecycleOwner, Observer {

            usersMatched.addAll(it)

            if (viewLoungeStatus == 3) {

                loungeAdapter.changeUsers(usersMatched)
                loungeAdapter.notifyDataSetChanged()
            }
        })

        viewModel.usersLikedUpdated.observe(viewLifecycleOwner, Observer {

            usersLiked.addAll(0, it)

            if (viewLoungeStatus == 2) {
                loungeAdapter.changeUsers(usersLiked)
                loungeAdapter.notifyDataSetChanged()

            } else {


            }

        })

        viewModel.usersMatchedUpdated.observe(viewLifecycleOwner, Observer {

            usersMatched.addAll(0, it)

            if (viewLoungeStatus == 3) {
                loungeAdapter.changeUsers(usersMatched)
                loungeAdapter.notifyDataSetChanged()

            } else {


            }


        })

        viewModel.getLoungeUser(2)

        viewModel.getLoungeUser(3)

        chatRoomAdapter = ChatRoomAdapter(roomList) {

            Log.d("message-get", it.toString())
            val intent = Intent(requireActivity(), RoomActivity::class.java)
            intent.putExtra("room", it)
            startActivity(intent)
        }

        binding.recyclerChatRoomList.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerChatRoomList.adapter = chatRoomAdapter



        viewModel.roomUpdated.observe(viewLifecycleOwner, Observer {

            roomList.add(it)

            chatRoomAdapter.changeRooms(roomList)
            chatRoomAdapter.notifyDataSetChanged()

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