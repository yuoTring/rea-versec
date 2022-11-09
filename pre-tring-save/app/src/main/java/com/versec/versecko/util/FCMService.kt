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
import com.versec.versecko.view.SplashActivity

class FCMService : FirebaseMessagingService() {

    companion object {

    }

    private val FCM_ID = "channel_FCM"


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        Log.d("fcm-message", "message: "+ message.toString())

        val intent = Intent(this, SplashActivity::class.java)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val fcmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =

            NotificationCompat.Builder(this, FCM_ID)
                .setSmallIcon(R.drawable.logo__small)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo__small))
                .setContentTitle(resources.getString(R.string.app_name))
                .setSound(fcmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val type = message.data.get(this.resources.getString(R.string.FCM_TYPE))

        val nickname = message.data.get(resources.getString(R.string.FCM_NICKNAME))

        if (type != null) {

            if (type.equals(resources.getString(R.string.FCM_TYPE_LIKED))) {


                notificationBuilder
                    .setContentText(nickname+resources.getString(R.string.FCM_CONTENTS_LIKED))



            } else if (type.equals(resources.getString(R.string.FCM_TYPE_MATCHED))) {

                notificationBuilder
                    .setContentText(nickname+resources.getString(R.string.FCM_CONTENTS_MATCHED))

            } else if (type.equals((resources.getString(R.string.FCM_TYPE_CHAT)).lowercase())) {

                notificationBuilder
                    .setContentText(nickname+resources.getString(R.string.FCM_CONTENTS_CHAT))

            } else if (type.equals(resources.getString(R.string.FCM_TYPE_STORY_LIKED))) {

                notificationBuilder
                    .setContentText(nickname+resources.getString(R.string.FCM_CONTENTS_STORY_LIKED))

            } else if (type.equals(resources.getString(R.string.FCM_TYPE_OTHER))) {

            }







        }

        notificationManager.notify(800,notificationBuilder.build())

    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel (notificationManager: NotificationManager) {

        val fcmChannelName = "FCM__Notification"
        val description = "FCM__Description"

        val fcmChannel = NotificationChannel(FCM_ID, fcmChannelName, NotificationManager.IMPORTANCE_HIGH)

        fcmChannel.description = description
        fcmChannel.enableLights(true)
        fcmChannel.lightColor = Color.BLUE
        fcmChannel.enableVibration(true)

        notificationManager.createNotificationChannel(fcmChannel)

    }

}