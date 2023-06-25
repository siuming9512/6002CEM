package com.example.patrol.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.patrol.model.Point
import com.example.patrol.model.PointTaskLog
import com.example.patrol.model.RouteTask
import com.example.patrol.model.TaskLog
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PointTaskLogViewModel: ViewModel() {
    private val _taskLogs = MutableStateFlow<List<PointTaskLog>>(emptyList())
    val pointTaskLogs: StateFlow<List<PointTaskLog>> = _taskLogs.asStateFlow()
    fun fetchPointTaskLogs(taskId: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/tasks/${taskId}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val routeTaskJson = response.body?.string()
                    val routeTask = Gson().fromJson(routeTaskJson, RouteTask::class.java)

                    var pointTaskLogArr = mutableListOf<PointTaskLog>()

                    for (point in routeTask.patrolRoute.points) {
                        var pointTaskLog = PointTaskLog(point.id, point, false, null, null, null, null)

                        for (taskLog in routeTask.patrolTaskLogs) {
                            if (point.id == taskLog.point.id) {
                                pointTaskLog.checked = true
                                pointTaskLog.taskLogId = taskLog.id
                                pointTaskLog.checkedTime = taskLog.createDate
                                pointTaskLog.attachmentCount = taskLog.taskLogAttachments.size
                            }
                        }

                        pointTaskLogArr.add(pointTaskLog)
                    }

                    _taskLogs.value = pointTaskLogArr.sortedBy { it.point.createDate }
                }
            }
        })
    }

//    fun addTaskLog(taskId: Int, pointId: Int): TaskLog? {
//        val client = OkHttpClient()
//
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val jsonObject = JSONObject().apply {
//            put("taskId", taskId)
//            put("pointId", pointId)
//        }
//        val request = Request.Builder()
//            .url("https://testapi20230527192318.azurewebsites.net/api/taskLogs")
//            .post(jsonObject.toString().toRequestBody(mediaType))
//            .build()
//
//        val response = client.newCall(request).execute()
//
//        if(response.isSuccessful) {
//            val json = response.body?.string()
//            val taskLog = Gson().fromJson(json, TaskLog::class.java)
//
//            var currentList = _taskLogs.value.toMutableList()
//
//            val index = currentList.indexOfFirst { it.id == pointId }
//            if (index != -1) {
//                currentList[index] = currentList[index].copy(checked = true, taskLogId = taskLog.id, checkedTime = taskLog.createDate)
//                _taskLogs.value = currentList
//            }
//
//            return taskLog
//        } else {
//            return null
//        }
//    }

    fun addTaskLog(taskId: Int, pointId: Int, callback: (TaskLog) -> Unit) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("taskId", taskId)
            put("pointId", pointId)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/taskLogs")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val json = response.body?.string()
                    val taskLog = Gson().fromJson(json, TaskLog::class.java)

                    var currentList = _taskLogs.value.toMutableList()

                    val index = currentList.indexOfFirst { it.id == pointId }
                    if (index != -1) {
                        currentList[index] = currentList[index].copy(checked = true, taskLogId = taskLog.id, checkedTime = taskLog.createDate)
                        _taskLogs.value = currentList
                    }

                    callback(taskLog)

//                    return taskLog
                }
            }
        })
    }
}