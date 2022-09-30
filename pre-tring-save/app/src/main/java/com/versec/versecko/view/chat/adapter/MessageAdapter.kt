package com.versec.versecko.view.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.AppContext
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.databinding.MessageMeBinding
import com.versec.versecko.databinding.MessageOtherBinding

class MessageAdapter (

    private var messageList : MutableList<MessageEntity>

        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        val VIEW_TYPE_ME = 0
        val VIEW_TYPE_OTHER = 1

    }



    inner class OtherViewHolder (val binding : MessageOtherBinding) : RecyclerView.ViewHolder(binding.root)
    inner class MeViewHolder (val binding : MessageMeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {

        if (AppContext.uid.equals(messageList.get(position).sender.uid)) {

            return VIEW_TYPE_ME

        }
        else {

            return VIEW_TYPE_OTHER
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ME) {

            val bindingMe = MessageMeBinding.inflate(LayoutInflater.from(parent.context),parent,false)


            return MeViewHolder(bindingMe)

        } else {

            val bindingOther = MessageOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return OtherViewHolder(bindingOther)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messageList.get(position)

        //Me Message
        if (holder.itemViewType == VIEW_TYPE_ME) {

            val viewHolder = holder as MeViewHolder

            viewHolder.binding.textMessage.setText(message.contents)

            // first message
            if (position == 0 ) {

                viewHolder.binding.textDate.visibility = View.VISIBLE
                viewHolder.binding.textDate.setText(message.date)



            } else {

                // Date Visible (current message date != former message date)
                if (!message.date.equals(messageList.get(position-1).date))
                {
                    viewHolder.binding.textDate.visibility = View.VISIBLE
                    viewHolder.binding.textDate.setText(message.date)
                } else {
                    viewHolder.binding.textDate.visibility = View.GONE
                }


            }

            // last message
            if (position == messageList.size -1 ) {

                viewHolder.binding.textTime.setText(message.time)

            } else {

                // Check who send message
                if (message.sender.uid.equals(messageList.get(position+1).sender.uid)) {

                    // Time Invisible (current message time == next message time )
                    if (message.time.equals(messageList.get(position+1).time)) {

                        viewHolder.binding.textTime.visibility = View.GONE
                    } else {
                        viewHolder.binding.textTime.visibility = View.VISIBLE
                        viewHolder.binding.textTime.setText(message.time)
                    }

                } else {

                    viewHolder.binding.textTime.visibility = View.VISIBLE
                    viewHolder.binding.textTime.setText(message.time)
                }

            }




            if (message.readed)
                viewHolder.binding.viewReadOrNot.visibility = View.INVISIBLE
            else
                viewHolder.binding.viewReadOrNot.visibility = View.VISIBLE

        }
        // Other Message
        else {

            val viewHolder = holder as OtherViewHolder

            viewHolder.binding.textMessage.setText(message.contents)

            // first message
            if (position == 0 ) {

                viewHolder.binding.textDate.visibility = View.VISIBLE
                viewHolder.binding.textDate.setText(message.date)

                viewHolder.binding.textNickName.setText(message.sender.nickName)

                Glide
                    .with(viewHolder.binding.root)
                    .load(message.sender.uri)
                    .into(viewHolder.binding.imageProfile)



            } else {

                // Date Visible (current message date != former message date)
                if (!message.date.equals(messageList.get(position-1).date))
                {
                    viewHolder.binding.textDate.visibility = View.VISIBLE
                    viewHolder.binding.textDate.setText(message.date)
                } else {
                    viewHolder.binding.textDate.visibility = View.GONE
                }

                // NickName and Image Invisible
                if (message.sender.uid.equals(messageList.get(position-1))) {

                    viewHolder.binding.textNickName.visibility = View.GONE
                    viewHolder.binding.imageProfile.visibility = View.INVISIBLE

                } else {

                    viewHolder.binding.textNickName.setText(message.sender.nickName)

                    Glide
                        .with(viewHolder.binding.root)
                        .load(message.sender.uri)
                        .into(viewHolder.binding.imageProfile)
                }


            }





            // last message
            if (position == messageList.size -1 ) {

                viewHolder.binding.textTime.setText(message.time)

            } else {

                // Check who send message
                if (message.sender.uid.equals(messageList.get(position+1).sender.uid)) {

                    // Time Invisible (current message time == next message time )
                    if (message.time.equals(messageList.get(position+1).time)) {

                        viewHolder.binding.textTime.visibility = View.GONE
                    } else {
                        viewHolder.binding.textTime.visibility = View.VISIBLE
                        viewHolder.binding.textTime.setText(message.time)
                    }

                } else {

                    viewHolder.binding.textTime.visibility = View.VISIBLE
                    viewHolder.binding.textTime.setText(message.time)
                }

            }

            viewHolder.binding.viewReadOrNot.visibility = View.INVISIBLE


        }
    }

    override fun getItemCount(): Int { return messageList.size }
    fun updateMessage(message : MessageEntity) { messageList.add(message)}
    fun changeMessages (messageList: MutableList<MessageEntity>) { this.messageList = messageList }


}