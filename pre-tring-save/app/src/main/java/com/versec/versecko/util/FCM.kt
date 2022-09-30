package com.versec.versecko.util

import org.json.JSONObject

class FCM {

    companion object {

        const val LIKED = 2
        const val MATCHED = 3
        const val CHAT = 4



        fun sendNotification (nickName : String, notificationType : Int, content : String) {

            val fcm = JSONObject()
            val body = JSONObject()

            var title = "~"

            if (notificationType == LIKED) {
                title = "Liked"
            } else if (notificationType == MATCHED) {
                title = "Matched"
            } else if (notificationType == CHAT) {
                title = "Chat"
            }

            try {

                body.put("title", title)
                body.put("content", content)
                //fcm.put("to")
                fcm.put("data", body)


            } catch (exception : Exception) {

            }
        }
    }


}