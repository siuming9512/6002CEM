package com.example.patrol.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
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

class RouteTaskViewModel: ViewModel() {
    private val _routeTasks = MutableStateFlow<List<RouteTask>>(emptyList())
    val routeTasks: StateFlow<List<RouteTask>> = _routeTasks.asStateFlow()

    init {
        fetchRouteTasks()
    }

    fun fetchRouteTasks() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/tasks")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val routeTasks = Gson().fromJson(json, Array<RouteTask>::class.java).toList()
                    _routeTasks.value = routeTasks
                    Log.i("RouteViewModel", routeTasks.toString())
                }
            }
        })
    }

    fun addRouteTask(routeTaskId: Int, userId: Int) {

        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("routeId", routeTaskId)
            put("userId", userId)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/tasks")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val routeTask = Gson().fromJson(json, RouteTask::class.java)

                    var currentList = _routeTasks.value.toMutableList()
                    currentList.add(routeTask)

                    _routeTasks.value = currentList
                }
            }
        })
    }
}

