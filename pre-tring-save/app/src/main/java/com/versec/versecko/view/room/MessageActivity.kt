package com.versec.versecko.view.room

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        binding.lifecycleOwner = this



        val intent = getIntent()

        room = intent.getSerializableExtra("room") as? RoomEntity
        roomUid = room!!.uid

        ownUid = AppContext.uid
        other = intent.getSerializableExtra("other") as RoomMemberEntity


        layoutManager = LinearLayoutManager(this)

        binding.recyclerMessages.layoutManager = layoutManager
        binding.recyclerMessages.recycledViewPool.setMaxRecycledViews(0,0)
        layoutManager.stackFromEnd = true
        layoutManager.isSmoothScrollbarEnabled = true


        adapter = MessageAdapter(messageList, ownUid, other)
        binding.recyclerMessages.adapter = adapter


        lifecycleScope.launch(Dispatchers.IO) {


            viewModel.observeMessage(roomUid).collect { map ->

                val type = map.keys.first()

                val messageResponse = map[type]

                if (type == ADDED) {


                    when(messageResponse) {

                        is Response.Success -> {

                            val message = messageResponse.data


                            if (messageList.contains(message)) {


                                val index = messageList.indexOf(message)

                                messageList.set(index, message)

                            } else {
                                messageList.add(message)
                            }


                            // in case of other sent message and received it
                            if (!message.read && !message.sender.equals(ownUid)) {


                                val readMessageResponse = viewModel.readMessage(roomUid, message.uid)

                                when(readMessageResponse) {

                                    is Response.Success -> {

                                    }
                                    is Response.Error -> {

                                    }
                                    else -> {

                                    }
                                }


                            } else {

                                withContext(Dispatchers.Main) {

                                    adapter.changeMessages(messageList)
                                    adapter.notifyDataSetChanged()
                                }
                            }


                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }

                    }


                } else if (type == CHANGED) {

                    when(messageResponse) {

                        is Response.Success -> {

                            val message = messageResponse.data


                            val index = messageList.indexOf(message)

                            messageList.set(index, message)

                            withContext(Dispatchers.Main) {

                                adapter.changeMessages(messageList)
                                adapter.notifyDataSetChanged()
                            }


                        }
                        is Response.Error -> {

                        }
                        else -> {

                        }

                    }

                } else if (type == RESPONSE_NULL) {

                }
                else if (type == ERROR) {

                }



            }

        }

        binding.buttonSend.setOnClickListener {

            if (binding.editMessage.text!!.length > 0) {

                lifecycleScope.launch {

                    val sendResponse = viewModel.sendMessage(binding.editMessage.text.toString(), roomUid)

                    when(sendResponse) {

                        is Response.Success -> {

                        }
                        is Response.Error -> {

                            Toast.makeText(this@MessageActivity, "인터넷 연결 오류로 인해 메시지가 전송되지 않았습니다. 잠시 기다렸다가 다시 보내주세요",
                                Toast.LENGTH_SHORT).show()
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

    companion object {

        const val ADDED = 1
        const val CHANGED = 2
        const val ERROR = -1
        const val RESPONSE_NULL = -2

    }





}