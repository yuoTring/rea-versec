package com.versec.versecko.view.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.versec.versecko.data.entity.MessageEntity
import com.versec.versecko.data.entity.RoomMemberEntity
import com.versec.versecko.databinding.MessageMeBinding
import com.versec.versecko.databinding.MessageOtherBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter (

    private var messageList : MutableList<MessageEntity>,
    private var ownUid : String,
    private var other : RoomMemberEntity

        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        val VIEW_TYPE_ME = 0
        val VIEW_TYPE_OTHER = 1

        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
        val TIME_FORMAT = SimpleDateFormat("HH:mm")

        const val VISIBLE = View.VISIBLE
        const val INVISIBLE = View.INVISIBLE
        const val GONE = View.GONE
    }



    inner class OtherViewHolder (val binding : MessageOtherBinding) : RecyclerView.ViewHolder(binding.root)
    inner class MeViewHolder (val binding : MessageMeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {


        if (ownUid.equals(messageList.get(position).sender))
            return VIEW_TYPE_ME
        else
            return VIEW_TYPE_OTHER
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

        val sender = message.sender
        val date = DATE_FORMAT.format(Timestamp(message.timestamp))
        val time = TIME_FORMAT.format(Timestamp(message.timestamp))
        val contents = message.contents
        val read = message.read





        //Own Message
        if (holder.itemViewType == VIEW_TYPE_ME) {

            val viewHolder = holder as MeViewHolder

            val textDate = viewHolder.binding.textDate
            val textTime = viewHolder.binding.textTime
            val textMessage = viewHolder.binding.textMessage
            val viewRead = viewHolder.binding.viewReadOrNot


            textMessage.setText(contents)

            // first message
            if (position == 0 ) {

                textDate.visibility = VISIBLE
                textDate.setText(date)


            } else {

                // Date Visible (current message date != former message date)

                val formerDate = DATE_FORMAT.format(Timestamp(messageList.get(position - 1).timestamp))


                if (!date.equals(formerDate)) {

                    textDate.visibility = VISIBLE
                    textDate.setText(date)

                } else {
                    textDate.visibility = GONE
                }

            }

            // last message
            if (position == messageList.size -1 ) {

                textTime.setText(time)

            } else {


                val nextMessage = messageList.get(position + 1)
                val nextTime = TIME_FORMAT.format(Timestamp(nextMessage.timestamp))

                //check who send message
                if (sender.equals(nextMessage.sender)) {

                    //Time Invisible (current message time == next message time)
                    if (time.equals(nextTime)) {

                        textTime.visibility = GONE

                    } else {


                        textTime.visibility = VISIBLE
                        textTime.setText(time)

                    }

                } else {

                    textTime.visibility = VISIBLE
                    textTime.setText(time)

                }

            }




            if (read)
                viewRead.visibility = GONE
            else
                viewRead.visibility = VISIBLE
        }
        // Other Message
        else {



            val viewHolder = holder as OtherViewHolder

            val nickName = other.nickName
            val url = other.profileUrl

            val textNickName = viewHolder.binding.textNickName
            val textDate = viewHolder.binding.textDate
            val textTime = viewHolder.binding.textTime
            val textMessage = viewHolder.binding.textMessage
            val viewRead = viewHolder.binding.viewReadOrNot
            val imageProfile = viewHolder.binding.imageProfile

            textMessage.setText(contents)

            // first message
            if (position == 0 ) {


                textDate.visibility = VISIBLE
                textDate.setText(date)
                textNickName.setText(nickName)

                Glide
                    .with(viewHolder.binding.root)
                    .load(url)
                    .into(imageProfile)



            } else {

                //Date Visible
                val formerDate = DATE_FORMAT.format(Timestamp(messageList.get(position - 1).timestamp))

                if (!date.equals(formerDate)) {

                    textDate.visibility = VISIBLE
                    textDate.setText(date)

                } else {
                    textDate.visibility = GONE
                }

                val formerMessage = messageList.get(position - 1)

                //NickName and Image Invisible
                if (sender.equals(formerMessage.uid)) {

                    textNickName.visibility = GONE
                    imageProfile.visibility = INVISIBLE

                } else {

                    textNickName.visibility = VISIBLE
                    textNickName.setText(nickName)

                    Glide
                        .with(viewHolder.binding.root)
                        .load(url)
                        .into(imageProfile)
                }





            }





            // last message
            if (position == messageList.size -1 ) {

                textTime.setText(time)

            } else {


                val nextMessage = messageList.get(position + 1)
                val nextTime = TIME_FORMAT.format(Timestamp(nextMessage.timestamp))

                if (sender.equals(nextMessage.sender)) {


                    if (time.equals(nextTime)) {

                        textTime.visibility = GONE

                    } else {

                        textTime.visibility = VISIBLE
                        textTime.setText(time)

                    }

                } else {

                    textTime.visibility = VISIBLE
                    textTime.setText(time)

                }

            }

            viewHolder.binding.viewReadOrNot.visibility = View.INVISIBLE


        }
    }

    override fun getItemCount(): Int { return messageList.size }
    fun changeMessages (messageList: MutableList<MessageEntity>) { this.messageList = messageList }




}