package com.versec.versecko.view.room

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.*
import com.versec.versecko.databinding.FragmentRoomBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.room.adapter.RoomAdapter
import com.versec.versecko.view.room.adapter.LoungeAdapter
import com.versec.versecko.view.matching.UserProfileActivity
import com.versec.versecko.viewmodel.RoomListViewModel
import com.versec.versecko.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class
RoomFragment : Fragment() {

    private lateinit var binding : FragmentRoomBinding
    private val viewModel : RoomListViewModel by viewModel<RoomListViewModel>()
    private val mainViewModel : MainViewModel by viewModel<MainViewModel>()

    private lateinit var loungeAdapter: LoungeAdapter
    private var viewLoungeStatus : Int = 2

    private var timestamp : Long = 0

    private val usersLiked : MutableList<LoungeUser> = mutableListOf()
    private val usersMatched : MutableList<LoungeUser> = mutableListOf()

    private lateinit var roomAdapter: RoomAdapter
    private var roomList : MutableList<RoomEntity> = mutableListOf()
    private var otherUserList : MutableList<RoomMemberEntity> = mutableListOf()
    private var notificationMap : MutableMap<String, Boolean> = mutableMapOf()

    private lateinit var ownUser : UserEntity
    private lateinit var otherUser : RoomMemberEntity

    private var roomUidList : MutableList<String> = mutableListOf()

    private lateinit var roomJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_room,container,false)
        val view = binding.root

        return view
    }

    @SuppressLint("NotifyDataSetChanged")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timestamp = viewModel.getTimestamp()


        mainViewModel.userLocal.observe(viewLifecycleOwner, Observer {
            ownUser = it
        })

        loungeAdapter = LoungeAdapter(userList = usersLiked, timestamp) {

            var intent = Intent(requireActivity(), UserProfileActivity::class.java)
            intent.putExtra("loungeUser", it)
            startActivity(intent)
        }

        // get liked users from FireStore
        lifecycleScope.launch {
            viewModel.likedUsers.collect { response ->

                when (response) {

                    is Response.Loading -> binding.progressBarLounge.show()
                    is Response.Success -> {

                        binding.progressBarLounge.hide()

                        val sortedMap = response.data.toSortedMap(compareByDescending { it })

                        usersLiked.clear()
                        sortedMap.forEach { entry -> usersLiked.add(mapUserEntityToLoungeUser(entry.value, entry.key)) }

                        Log.d("liked-confirm", "liked: "+usersLiked.toString())



                        // check which tab is selected
                        if (viewLoungeStatus == 2 ) {

                            loungeAdapter.changeUsers(usersLiked)
                            loungeAdapter.notifyDataSetChanged()

                        } else {

                            // check new liked users and if yes, set badge on tab to notify user it
                            if (usersLiked.size > 0 )
                                if (usersLiked.get(0).timestamp > timestamp)
                                    binding.tabLayout.getTabAt(0)?.orCreateBadge!!.number

                        }

                    }
                    is Response.Error -> {
                        binding.progressBarLounge.show()

                        Log.d("lounge-confirm", response.errorMessage)
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

                        val sortedMap = response.data.toSortedMap(compareByDescending { it })

                        usersMatched.clear()
                        sortedMap.forEach { entry ->  usersMatched.add(mapUserEntityToLoungeUser(entry.value, entry.key)) }

                        // check which tab is selected
                        if (viewLoungeStatus == 3 ) {

                            loungeAdapter.changeUsers(usersMatched)
                            loungeAdapter.notifyDataSetChanged()

                        } else {

                            // check new matched users and if yes, set badge on tab to notify user it
                            if (usersMatched.size > 0 )
                                if (usersMatched.get(0).timestamp > timestamp)
                                    binding.tabLayout.getTabAt(1)?.orCreateBadge!!.number



                        }
                    }
                    is Response.Error -> {
                        binding.progressBarLounge.show()
                        Log.d("lounge-confirm", response.errorMessage)
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
                        }

                        viewLoungeStatus = 2

                        binding.tabLayout.getTabAt(0)?.removeBadge()

                        loungeAdapter.changeUsers(usersLiked)
                        loungeAdapter.notifyDataSetChanged()

                    } else {

                        if (viewLoungeStatus ==2) {
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




        roomAdapter = RoomAdapter(roomList, otherUserList, notificationMap) {


            val room = it

            if (room != null) {


                lifecycleScope.launch {

                    val updateResponse = viewModel.updateLastRead(it.uid)
                    when (updateResponse) {

                        is Response.Success -> {

                            notificationMap.set(room.uid, false)
                            roomAdapter.changeNotificationMap(notificationMap)
                            roomAdapter.notifyDataSetChanged()
                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }

                }

                val intent = Intent(requireActivity(), MessageActivity::class.java)
                intent.putExtra("room", it)
                intent.putExtra("other", otherUser)
                startActivity(intent)

            } else {

                Toast.makeText(requireActivity(), "$$$", Toast.LENGTH_SHORT).show()
            }

        }

        binding.recyclerChatRoomList.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerChatRoomList.adapter = roomAdapter




    }

    override fun onStart() {
        super.onStart()

        fetchTimestamp()


        roomUidList.clear()
        roomList.clear()

        updateRooms()
    }

    override fun onStop() {
        super.onStop()

        updateTimestamp()
        roomJob.cancel()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden)
            updateTimestamp()
        else
            fetchTimestamp()

    }

    private fun updateTimestamp () { viewModel.setTimestamp() }
    private fun fetchTimestamp () { this.timestamp = viewModel.getTimestamp() }

    private fun checkNotification (roomUid : String) {


        lifecycleScope.launch {

            val getLastMessageResponse = viewModel.getLastMessage(roomUid)

            when(getLastMessageResponse) {

                is Response.Success -> {

                    val lastMessage = getLastMessageResponse.data

                    Log.d("last-message-check", lastMessage.toString())


                    val getOwnLastRead = viewModel.getOwnLastRead(roomUid)

                    when(getOwnLastRead) {

                        is Response.Success -> {

                            val ownLastRead = getOwnLastRead.data

                            Log.d("room-check-ownLastRead", ownLastRead.toString())

                            if (lastMessage.timestamp > ownLastRead) {

                                notificationMap.set(roomUid, true)

                            } else {
                                notificationMap.set(roomUid, false)
                            }

                            Log.d("room-check-notification", notificationMap.size.toString())

                            roomAdapter.changeNotificationMap(notificationMap)
                            roomAdapter.notifyDataSetChanged()
                        }
                        is Response.No -> {

                        }
                        is Response.Error -> {
                            Log.d("room-check-lastRead!!!", getOwnLastRead.errorMessage)
                        }
                        else -> {

                        }
                    }

                }
                is Response.Error -> {

                }
                else -> {

                }
            }

        }

    }

    private fun updateRooms () {

        roomJob = lifecycleScope.launch {



            viewModel.getRoomUid().collect {


                val key = it.keys.first()


                if (key == ADDED) {

                    binding.progressBarChatRoom.hide()

                    val response = it.get(ADDED)

                    //1-1 get room uid added to rdb
                    when(response) {
                        is Response.Success -> {

                            val roomUid = response.data.uid

                            Log.d("room-check-roomUid", roomUid)



                            if (!roomUidList.contains(roomUid)){
                                roomUidList.add(roomUid)
                            }
                            else {

                                roomUidList.forEachIndexed { index, uid ->

                                    if (uid.equals(roomUid))
                                        roomUidList.set(index, roomUid)

                                }
                            }

                            val getRoomForOneShotResponse = viewModel.getRoomForOneShot(roomUid)

                            //2. get room from realtime database for one shot
                            when(getRoomForOneShotResponse) {

                                is Response.Success -> {

                                    val roomEntity = getRoomForOneShotResponse.data



                                    if (roomEntity != null) {

                                        Log.d("room-check-room", roomEntity.toString())


                                        var otherUid = EMPTY_UID

                                        roomEntity.members.forEach { uid ->

                                            if (!uid.equals(AppContext.uid))
                                                otherUid = uid
                                        }




                                        //3. get other room member
                                        val getUserResponse = viewModel.getUser(otherUid)

                                        when(getUserResponse) {
                                            is Response.Success -> {

                                                otherUser = getUserResponse.data




                                                if (!otherUserList.contains(otherUser)) {

                                                    otherUserList.add(otherUser)

                                                    if (!roomList.contains(roomEntity))
                                                        roomList.add(roomEntity)

                                                    roomAdapter.changeRooms(roomList)
                                                    roomAdapter.changeOtherUsers(otherUserList)
                                                    roomAdapter.notifyDataSetChanged()

                                                } else {

                                                    otherUserList.add(otherUser)

                                                    if (!roomList.contains(roomEntity))
                                                        roomList.add(roomEntity)

                                                    roomAdapter.changeRooms(roomList)
                                                    roomAdapter.changeOtherUsers(otherUserList)
                                                    roomAdapter.notifyDataSetChanged()

                                                    /**
                                                    otherUserList.forEachIndexed { index, roomMember ->

                                                    otherUserList.set(index, otherUser)


                                                    if (!roomList.contains(roomEntity))
                                                    roomList.add(roomEntity)


                                                    Log.d("room-check-roomList", "size: "+roomList.size)
                                                    roomAdapter.changeRooms(roomList)
                                                    roomAdapter.changeOtherUsers(otherUserList)
                                                    roomAdapter.notifyDataSetChanged()
                                                    } **/

                                                }

                                                checkNotification(roomEntity.uid)




                                                //3. add listener to lastMessage (lastSend in rdb) to catch a last message of each room
                                                viewModel.addListenerLastMessage(roomEntity.uid).observe(viewLifecycleOwner, Observer { lastSend ->

                                                    when(lastSend) {

                                                        is Response.Success -> {
                                                            Log.d("room-last", lastSend.data)

                                                            roomList.forEach { room ->

                                                                if (room.uid.equals(roomEntity.uid))
                                                                    room.lastSent = lastSend.data

                                                                roomAdapter.changeRooms(roomList)
                                                                roomAdapter.notifyDataSetChanged()
                                                            }

                                                            checkNotification(roomEntity.uid)
                                                        }
                                                        is Response.Error -> {

                                                            Log.d("room-check-lastMessage", lastSend.errorMessage)

                                                        }
                                                        else -> {

                                                        }
                                                    }
                                                } )

                                            }
                                            is Response.No -> {

                                                Log.d("room-check-room", "no")

                                            }
                                            is Response.Error -> {
                                                Log.d("room-check-room!!!", "error: "+ getUserResponse.errorMessage)

                                            }
                                            else -> {

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
                            binding.progressBarChatRoom.show()
                        }
                    }

                } else if (key == REMOVED) {

                    binding.progressBarChatRoom.show()

                } else if (key == CHANGED) {

                    binding.progressBarChatRoom.show()

                }
                else if (key == ERROR) {
                    binding.progressBarChatRoom.show()
                }

            }

        }
    }



    companion object {

        val ADDED = 1
        val REMOVED = 0
        val CHANGED = 2
        val ERROR = -1

        const val EMPTY_UID = "~"

        @JvmStatic
        fun newInstance() =
            RoomFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun mapUserEntityToLoungeUser ( userEntity: UserEntity, timestamp : Long ) : LoungeUser {

        val loungeUser = LoungeUser (

            uid = userEntity.uid,
            nickName = userEntity.nickName,
            gender = userEntity.gender,
            birth = userEntity.birth,
            mainResidence = userEntity.mainResidence,
            subResidence = userEntity.subResidence,
            tripWish = userEntity.tripWish,
            tripStyle = userEntity.tripStyle,
            selfIntroduction = userEntity.selfIntroduction,

            uriMap = userEntity.uriMap,
            geohash = userEntity.geohash,
            latitude = userEntity.latitude,
            longitude = userEntity.longitude,
            languages = userEntity.languages,
            deletedAt = userEntity.deletedAt,

            mannerScore = userEntity.mannerScore,
            premiumOrNot = userEntity.premiumOrNot,
            knock = userEntity.knock,

            loungeStatus = userEntity.loungeStatus,
            phoneNumber = userEntity.phoneNumber,

            timestamp = timestamp

                )

        return loungeUser
    }
}