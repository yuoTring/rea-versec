package com.versec.versecko.view.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.versec.versecko.AppContext
import com.versec.versecko.R
import com.versec.versecko.data.entity.RoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.databinding.ActivityRoomBinding
import com.versec.versecko.util.Response
import com.versec.versecko.view.chat.adapter.MessageAdapter
import com.versec.versecko.viewmodel.RoomViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRoomBinding
    private val viewModel : RoomViewModel by viewModel<RoomViewModel>()
    private var room : RoomEntity? = RoomEntity()

    private val messageList = mutableListOf<MessageEntity>()
    private lateinit var adapter: MessageAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        binding.lifecycleOwner = this



        val intent = getIntent()

        room = intent.getSerializableExtra("room") as? RoomEntity
        resetUnreadMessageCounter()

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        binding.recyclerMessages.layoutManager = layoutManager
        binding.recyclerMessages.recycledViewPool.setMaxRecycledViews(0,0)


        adapter = MessageAdapter(messageList)
        binding.recyclerMessages.adapter = adapter

        viewModel.getMessages(room!!.uid).observe(this, Observer {


            if (messageList.contains(it)) {

                val index = messageList.indexOf(it)
                messageList.set(index, it)
            } else {
                messageList.add(it)
            }

            //if (it.readed == false && it.receiver.uid == AppContext.uid)
                resetReadOrNot(it)

            adapter.changeMessages(messageList)
            adapter.notifyDataSetChanged()

            binding.recyclerMessages.scrollToPosition(messageList.size-1)
        })


        binding.buttonSend.setOnClickListener {

            if (binding.editMessage.text!!.length>0) {


                lifecycleScope.launch {

                    viewModel.sendMessage(binding.editMessage.text.toString(), room!!)


                }
            }
        }

        binding.editMessage.doAfterTextChanged { text ->

            if (text.toString().length>0) {
                binding.buttonSend.visibility = View.VISIBLE
            } else {
                binding.buttonSend.visibility = View.GONE
            }
        }





    }

    private fun resetUnreadMessageCounter () {
        lifecycleScope.launch {

            val response = viewModel.resetUnreadMessageCounter(room!!.uid)

            when(response) {
                is Response.Success -> {

                }
                is Response.Error -> {
                    Log.d("error", "resetUnreadMessageCounter: "+response.errorMessage)
                }
                else -> {

                }
            }
        }
    }

    private fun resetReadOrNot (message : MessageEntity) {

        lifecycleScope.launch {
            val response = viewModel.resetReadOrNot(message)

            when(response) {
                is Response.Success -> {

                }
                is Response.Error -> {
                    Log.d("error", "resetReadOrNot: "+response.errorMessage)
                }
                else -> {

                }
            }
        }
    }


}