package com.versec.versecko.util

import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class FCM {

    companion object {

        const val LIKED = 2
        const val MATCHED = 3
        const val CHAT = 4


        suspend fun sendNotification(nickName : String, notificationType : Int, content : String, receiverToken : String) {

            coroutineScope {


                val serverKey = "AAAAadTCw1I:APA91bHvSsTlbB9vujyk2MkMQ8lEEk7rZWOGzngITbtuw-_cI17vNfeKB_Kd_p5SyjJ-5OsKvWcQ4vcBCnpcsuWG2patQw2qNRdLXrPc5WIsFtz0DIlXyJfdwrXj54gj3ozMbprYAc3g"

                val httpClient = OkHttpClient()




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
                    body.put("type", notificationType)
                    body.put("sender", nickName)

                    fcm.put("to", receiverToken)
                    fcm.put("data", body)


                    val requestBody
                    = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), fcm.toString())

                    val request = Request.Builder()
                        .header("Authorization", serverKey)
                        .url("https://fcm.googleapis.com/fcm/send")
                        .put(requestBody)
                        .build()

                    val response = httpClient.newCall(request).execute()
                    val lastResponse = response.body.toString()


                } catch (exception : Exception) {


                }

            }
        }
    }


}