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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.versec.versecko.R
import com.versec.versecko.data.entity.Room
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.UserEntity
import com.versec.versecko.databinding.FragmentChatBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.chat.adapter.RoomAdapter
import com.versec.versecko.view.chat.adapter.LoungeAdapter
import com.versec.versecko.view.matching.UserProfileActivity
import com.versec.versecko.viewmodel.RoomListViewModel
import com.versec.versecko.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RoomFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding
    private val viewModel : RoomListViewModel by viewModel<RoomListViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var loungeAdapter: LoungeAdapter
    private var viewLoungeStatus : Int = 2
    private var usersLiked : MutableList<UserEntity> = mutableListOf()
    private var usersMatched : MutableList<UserEntity> = mutableListOf()

    private var counterLiked : Int? = 0
    private var counterMatching : Int? = 0



    private lateinit var roomAdapter: RoomAdapter
    private var roomList : MutableList<RoomEntity> = mutableListOf()

    private lateinit var ownUser : UserEntity

    private var roomUidList : MutableList<Room> = mutableListOf()



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

        mainViewModel.userLocal.observe(viewLifecycleOwner, Observer {
            ownUser = it
        })

        viewModel.setCounter(2,0)
        viewModel.setCounter(3,0)
        // get a former lounge counter
        counterLiked = viewModel.getCounter(2)
        counterMatching = viewModel.getCounter(3)

        loungeAdapter = LoungeAdapter(usersLiked,0) {

            var intent = Intent(requireActivity(), UserProfileActivity::class.java)
            intent.putExtra("otherUser", it)
            startActivity(intent)
        }

        // get liked users from FireStore
        lifecycleScope.launch {
            viewModel.likedUsers.collect { response ->

                when (response) {

                    is Response.Loading -> binding.progressBarLounge.show()
                    is Response.Success -> {
                        binding.progressBarLounge.hide()

                        usersLiked.clear()
                        usersLiked.addAll(response.data)

                        // check which tab is selected
                        if (viewLoungeStatus == 2 ) {

                            // initialize added value
                            loungeAdapter.changeAddedValue(0)

                            //show new added users more apparently (stroke will be seen)
                            if (usersLiked.size > counterLiked!!)
                            {
                                viewModel.setCounter(2, usersLiked.size)
                                loungeAdapter.changeAddedValue(usersLiked.size - counterLiked!!)
                            }

                            loungeAdapter.changeUsers(usersLiked)
                            loungeAdapter.notifyDataSetChanged()

                        } else {

                            // check new liked users and if yes, set badge on tab to notify user it
                            if (usersLiked.size > counterLiked!!) {
                                binding.tabLayout.getTabAt(0)?.orCreateBadge!!.number
                            }

                        }

                    }
                    is Response.Error -> {
                        binding.progressBarLounge.show()
                    }
                    else -> {
                    }

                }

            }
        }

        lifecycleScope.launch {
            viewModel.matchingUsers.collect { response ->

                when (response) {

                    is Response.Loading -> binding.progressBarLounge.show()
                    is Response.Success -> {
                        binding.progressBarLounge.hide()

                        usersMatched.clear()
                        usersMatched.addAll(response.data)

                        // check which tab is selected
                        if (viewLoungeStatus == 3 ) {

                            // initialize added value
                            loungeAdapter.changeAddedValue(0)

                            //show new added users more apparently
                            if (usersMatched.size > counterLiked!!)
                            {
                                viewModel.setCounter(3, usersMatched.size)
                                loungeAdapter.changeAddedValue(usersMatched.size - counterLiked!!)
                            }

                            loungeAdapter.changeUsers(usersMatched)
                            loungeAdapter.notifyDataSetChanged()

                        } else {

                            // check new matched users and if yes, set badge on tab to notify user it
                            Log.d("lounge-check", "um: "+usersMatched.size)
                            Log.d("lounge-check", "cm: "+counterMatching)

                            if (usersMatched.size > counterMatching!!) {
                                binding.tabLayout.getTabAt(1)?.orCreateBadge!!.number
                            }

                        }

                    }
                    is Response.Error -> {
                        binding.progressBarLounge.show()
                    }
                    else -> {

                    }

                }



            }
        }

        binding.recyclerLikeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerLikeList.adapter = loungeAdapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab != null) {

                    if (tab.position == 0) {

                        if (viewLoungeStatus == 3) {
                            viewModel.setCounter(3, usersMatched.size)
                        }

                        viewLoungeStatus = 2

                        binding.tabLayout.getTabAt(0)?.removeBadge()

                        loungeAdapter.changeUsers(usersLiked)
                        loungeAdapter.notifyDataSetChanged()

                    } else {

                        if (viewLoungeStatus ==2) {
                            viewModel.setCounter(2, usersLiked.size)
                        }

                        viewLoungeStatus = 3

                        binding.tabLayout.getTabAt(1)?.removeBadge()

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




        roomAdapter = RoomAdapter(roomList) {

            val intent = Intent(requireActivity(), RoomActivity::class.java)
            intent.putExtra("room", it)
            startActivity(intent)


        }

        binding.recyclerChatRoomList.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerChatRoomList.adapter = roomAdapter


        lifecycleScope.launch {

            viewModel.roomsUid.collect {



                val key = it.keys.first()

                if (key == ADDED) {

                    binding.progressBarChatRoom.hide()

                    val response = it.get(ADDED)

                    //1-1 get room uid added to rdb
                    when(response) {
                        is Response.Success -> {


                            val roomUid : Room = response.data


                            val getRoomOneShotResponse = viewModel.getRoomForOneShot(roomUid.uid)

                            //2. get room for one shot
                            when(getRoomOneShotResponse) {

                                is Response.Success -> {

                                    val roomEntity = getRoomOneShotResponse.data



                                    if (roomEntity != null) {


                                        //3. add listener to each room entity to get a last sent
                                        viewModel.addListenerToRoom(roomEntity.uid).collect {

                                            when(it) {

                                                is Response.Success -> {

                                                }
                                                is Response.Error -> {

                                                }
                                                else -> {

                                                }
                                            }
                                        }



                                    } else {

                                    }



                                }
                                is Response.Error -> {

                                }
                                else -> {

                                }
                            }

                        }
                        else -> {

                        }
                    }

                } else if (key == REMOVED) {

                    binding.progressBarChatRoom.hide()

                    val response = it.get(REMOVED)

                    //1-2 get room uid removed from rdb
                    when(response) {
                        is Response.Success -> {

                            var targetIndex = 0

                            roomList.forEachIndexed { index, chatRoomEntity ->
                                if (chatRoomEntity.uid.equals(response.data.uid))
                                    targetIndex = index
                            }

                            roomList.removeAt(targetIndex
                            )
                            roomAdapter.changeRooms(roomList)
                            roomAdapter.notifyDataSetChanged()

                        }
                        else -> {

                        }
                    }

                } else if (key == CHANGED) {

                    val response = it.get(CHANGED)

                    //1-3 get room uid changed in rdb
                    when(response) {
                        is Response.Success -> {

                            var targetIndex = 0

                            roomList.forEachIndexed { index, chatRoomEntity ->
                                if (chatRoomEntity.uid.equals(response.data.uid))
                                    targetIndex = index
                            }


                            roomAdapter.changeRooms(roomList)
                            roomAdapter.notifyDataSetChanged()

                        }
                        else -> {

                        }
                    }



                }
                else if (key == ERROR) {
                        binding.progressBarChatRoom.show()
                }
             }

        }






        binding.buttonTest.setOnClickListener {

        }

    }

    override fun onStop() {
        super.onStop()
        reset()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        reset()
    }

    private fun reset () {

        if (viewLoungeStatus == 2) {

            counterLiked = viewModel.getCounter(2)
            loungeAdapter.changeAddedValue(0)
            loungeAdapter.changeUsers(usersLiked)
            loungeAdapter.notifyDataSetChanged()

        } else {
            counterMatching = viewModel.getCounter(3)
            loungeAdapter.changeAddedValue(0)
            loungeAdapter.changeUsers(usersMatched)
            loungeAdapter.notifyDataSetChanged()
        }
    }

    companion object {

        val ADDED = 1
        val REMOVED = 0
        val CHANGED = 2
        val ERROR = -1

        @JvmStatic
        fun newInstance() =
            RoomFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}