package com.versec.versecko.view.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.versec.versecko.R
import com.versec.versecko.data.entity.ChatRoomEntity
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.databinding.ActivityRoomBinding
import com.versec.versecko.view.chat.adapter.MessageAdapter
import com.versec.versecko.viewmodel.RoomViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRoomBinding
    private val viewModel : RoomViewModel by viewModel<RoomViewModel>()
    private var room : ChatRoomEntity? = ChatRoomEntity()

    private val messageList = mutableListOf<MessageEntity>()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        binding.lifecycleOwner = this

        val intent = getIntent()

        room = intent.getSerializableExtra("room") as? ChatRoomEntity

        binding.recyclerMessages.layoutManager = LinearLayoutManager(this)


        adapter = MessageAdapter(messageList)
        binding.recyclerMessages.adapter = adapter

        viewModel.getAllMessage(room!!.chatRoomUid).observe(this, Observer {

            adapter.changeMessages(it)
            adapter.notifyDataSetChanged()

        })


        binding.buttonSend.setOnClickListener {

            if (binding.editMessage.text!!.length>0) {

                viewModel.sendMessage(binding.editMessage.text!!.toString(), room!!)

            }
        }

        viewModel.set(room!!.chatRoomUid).observe(this, Observer {

            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show()
        })



    }


}