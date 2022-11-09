package com.versec.versecko.view.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.RoomMemberEntity
import com.versec.versecko.databinding.ActivityMessageBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.room.adapter.MessageAdapter
import com.versec.versecko.viewmodel.MessageViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMessageBinding
    private val viewModel : MessageViewModel by viewModel<MessageViewModel>()
    private var room : RoomEntity? = RoomEntity()

    private lateinit var roomUid : String
    private lateinit var ownUid : String
    private lateinit var other : RoomMemberEntity

    private val messageList = mutableListOf<MessageEntity>()
    private lateinit var adapter: MessageAdapter
    private lateinit var layoutManager: LinearLayoutManager


    private lateinit var fetchJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        binding.lifecycleOwner = this


        val intent = intent

        room = intent.getSerializableExtra("room") as RoomEntity
        roomUid = room!!.uid

        other = intent.getSerializableExtra("other") as RoomMemberEntity
        ownUid = AppContext.uid

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.isSmoothScrollbarEnabled = true

        binding.recyclerMessages.recycledViewPool.setMaxRecycledViews(0,0)
        binding.recyclerMessages.layoutManager = layoutManager

        adapter = MessageAdapter(messageList, ownUid, other)
        binding.recyclerMessages.adapter = adapter

        binding.buttonSend.setOnClickListener {

            val text = binding.editMessage.text.toString()

            if (text.length > 0) {

                binding.editMessage.text!!.clear()

                lifecycleScope.launch(Dispatchers.IO) {

                    val sendResponse = viewModel.sendMessage(text, roomUid)

                    when(sendResponse) {

                        is Response.Error -> {

                            withContext(Dispatchers.Main) {

                                Toast.makeText(this@MessageActivity,"인터넷 오류로 인해 메시지가 전송되지 않았습니다.",Toast.LENGTH_SHORT)
                            }

                        }
                        else -> {

                        }
                    }
                }

            }

        }



        binding.editMessage.doAfterTextChanged { text ->

            if (text!!.length > 0)
                binding.buttonSend.visibility = View.VISIBLE
            else
                binding.buttonSend.visibility = View.GONE

        }





    }

    override fun onStart() {
        super.onStart()

        fetchJob =
            lifecycleScope.launch(Dispatchers.IO) {


                viewModel.fetchMessage(roomUid).collect {  fetchResponse->


                    when (fetchResponse) {


                        is Response.Success -> {

                            val map = fetchResponse.data

                            val type = map.keys.first()

                            val message = map.get(type)

                            if (message != null) {


                                if (messageList.contains(message)) {

                                    val index = messageList.indexOf(message)
                                    messageList.set(index, message)

                                } else {

                                    messageList.add(message)

                                }


                                if (type == ADDED) {

                                    if (message.read == false && !message.sender.equals(ownUid)) {

                                        val readResponse = viewModel.readMessage(roomUid, message.uid)

                                        when(readResponse) {
                                            is Response.Error -> {

                                            }
                                            else -> {

                                            }
                                        }
                                    }
                                }

                                withContext(Dispatchers.Main) {

                                    adapter.changeMessages(messageList)
                                    adapter.notifyDataSetChanged()

                                }

                            } else {

                            }

                        }
                        is Response.No -> {

                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }
                    }


                }
            }
    }

    override fun onStop() {
        super.onStop()

        fetchJob.cancel()
    }

    companion object {

        const val ADDED = 1
        const val CHANGED = 2
        const val ERROR = -1
        const val RESPONSE_NULL = -2

    }





}