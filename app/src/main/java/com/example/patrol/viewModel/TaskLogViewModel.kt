package com.example.patrol.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.patrol.model.Attachment
import com.example.patrol.model.TaskLog
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

class TaskLogViewModel: ViewModel() {
    private val _taskLog = MutableStateFlow<TaskLog?>(null)
    val taskLog: StateFlow<TaskLog?> = _taskLog.asStateFlow()

    fun fetchTaskLog(taskLogId: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/taskLogs/${taskLogId}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val taskLog = Gson().fromJson(json, TaskLog::class.java)
                    _taskLog.value = taskLog
                }
            }
        })
    }

    fun addAttachment(taskLogId: Int, image: File) {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg", image.asRequestBody("image/jpg".toMediaTypeOrNull()))
            .build()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/attachments")
            .post(multipartBody)
            .build()

//        var response = client.newCall(request).execute()
//
//        if (response.isSuccessful) {
//            val json = response.body?.string()
//            val attachment = Gson().fromJson(json, Attachment::class.java)
//
//            if(attachment != null) {
//                val client = OkHttpClient()
//
//                val mediaType = "application/json; charset=utf-8".toMediaType()
//                val jsonObject = JSONObject().apply {
//                    put("attachmentIds", JSONArray(arrayListOf(attachment.id)))
//                }
//
//                var jsonStr = jsonObject.toString().toRequestBody(mediaType)
//
//                val request = Request.Builder()
//                    .url("https://testapi20230527192318.azurewebsites.net/api/taskLogs/${taskLogId}/attachments")
//                    .post(jsonStr)
//                    .build()
//                val response = client.newCall(request).execute()
//                val taskLogAttachmentJson = response.body?.string()
//                if (response.isSuccessful) {
//                    val taskLog = Gson().fromJson(taskLogAttachmentJson, TaskLog::class.java)
//                    _taskLog.value = taskLog
//                }
//            }
//
//        }
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val attachment = Gson().fromJson(json, Attachment::class.java)

                    if(attachment != null) {
                        val client = OkHttpClient()

                        val mediaType = "application/json; charset=utf-8".toMediaType()
                        val jsonObject = JSONObject().apply {
                            put("attachmentIds", JSONArray(arrayListOf(attachment.id)))
                        }

                        var jsonStr = jsonObject.toString().toRequestBody(mediaType)

                        val request = Request.Builder()
                            .url("https://testapi20230527192318.azurewebsites.net/api/taskLogs/${taskLogId}/attachments")
                            .post(jsonStr)
                            .build()
                        val response = client.newCall(request).execute()
                        val taskLogAttachmentJson = response.body?.string()
                        if (response.isSuccessful) {
                            val taskLog = Gson().fromJson(taskLogAttachmentJson, TaskLog::class.java)
                            _taskLog.value = taskLog
                        }
                    }

                }
            }
        })
    }
}