package com.versec.versecko.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.versec.versecko.R
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.view.MainScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FCMServic : FirebaseMessagingService() {

    private val userRepository : UserRepository by inject<UserRepository>()
    private val FCM_ID ="channel_fcm"


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, MainScreenActivity::class.java)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            setNotificationChannel(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, FCM_ID)
            .setSmallIcon(R.drawable.icon_chat)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_chat))
            .setContentTitle(message.data.get("title"))
            .setContentText(message.data.get("content"))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1000, notificationBuilder.build())
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch {

            when(userRepository.postFCMToken(token)) {
                is Response.Success -> {
                    Log.d("fcm-log", "post success")
                }
                is Response.Error -> { Log.d("fcm-log", "post error") }
                else -> {

                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationChannel (notificationManager: NotificationManager) {

        val fcmChannelName = "fcmNotification"
        val description = "fcm_description"

        val fcmChannel = NotificationChannel(FCM_ID, fcmChannelName,NotificationManager.IMPORTANCE_HIGH)

        fcmChannel.description = description
        fcmChannel.enableLights(true)
        fcmChannel.lightColor = Color.RED
        fcmChannel.enableVibration(true)

        notificationManager.createNotificationChannel(fcmChannel)


    }
}