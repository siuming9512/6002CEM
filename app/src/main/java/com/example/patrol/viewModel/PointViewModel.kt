package com.example.patrol.viewModel

import androidx.lifecycle.ViewModel
import com.example.patrol.model.Point
import com.example.patrol.model.RouteTask
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

class PointViewModel: ViewModel() {
    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    fun deletePoint(pointId: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/points/${pointId}")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    _points.value = _points.value.filter { it.id != pointId }
                }
            }
        })
    }

    fun addPoint(name: String, tagId: String, routeId: Int, lat: Double, lng: Double) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("pointName", name)
            put("tagId", tagId)
            put("routeId", routeId)
            put("latitude", lat)
            put("longitude", lng)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/points")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if(response.isSuccessful) {
                    val point = Gson().fromJson(json, Point::class.java)
                    var currentList = _points.value.toMutableList()
                    currentList.add(point)

                    _points.value = currentList
                }
            }
        })
    }

    fun editPoint(id: Int, name: String, tagId: String, lat: Double, lng: Double) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("pointName", name)
            put("tagId", tagId)
            put("latitude", lat)
            put("longitude", lng)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/points/${id}")
            .put(jsonObject.toString().toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if(response.isSuccessful) {
                    var currentList = _points.value.toMutableList()

                    val index = currentList.indexOfFirst { it.id == id }
                    if (index != -1) {
                        currentList[index] = currentList[index].copy(name = name, tagId = tagId, latitude = lat, longitude = lng)
                        _points.value = currentList
                    }
                }
            }
        })
    }


    fun fetchPoints(routeId: Int) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/routes/${routeId}/points")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val points = Gson().fromJson(json, Array<Point>::class.java).toList()
                    _points.value = points
                }
            }
        })
    }
}

