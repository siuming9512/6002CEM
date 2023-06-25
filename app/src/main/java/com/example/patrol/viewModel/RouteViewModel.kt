package com.example.patrol.viewModel

import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patrol.model.Route
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class RouteViewModel: ViewModel() {
    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes.asStateFlow()

    init {
        fetchRoutes()
    }

    fun fetchRoutes() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/routes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val routes = Gson().fromJson(json, Array<Route>::class.java).toList()
                    _routes.value = routes
                    Log.i("RouteViewModel", routes.toString())
                }
            }
        })
    }
//    fun fetchRoutes() {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://testapi20230527192318.azurewebsites.net/api/routes")
//            .build()
//        val response = client.newCall(request).execute()
//        try {
//
//            if (response.isSuccessful) {
//                val json = response.body?.string()
//                val routes = Gson().fromJson(json, Array<Route>::class.java).toList()
//                _routes.value = routes
//                Log.i("RouteViewModel", routes.toString())
//            }
//        } catch (ex: java.lang.Exception) {
//
//        }
//    }

    fun addRoute(name: String, desc: String?) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("routeName", name)
            put("description", desc)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/routes")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (response.isSuccessful) {
                    val route = Gson().fromJson(json, Route::class.java)
                    var currentList = _routes.value.toMutableList()
                    currentList.add(route)

                    _routes.value = currentList
                }
            }
        })
    }

    fun editRoute(id: Int, name: String, desc: String?) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("routeName", name)
            put("description", desc)
        }
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/routes/${id}")
            .put(jsonObject.toString().toRequestBody(mediaType))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (response.isSuccessful) {
                    var currentList = _routes.value.toMutableList()

                    val index = currentList.indexOfFirst { it.id == id }
                    if (index != -1) {
                        currentList[index] = currentList[index].copy(name = name, desc = desc)
                        _routes.value = currentList
                    }
                }
            }
        })
    }

    fun deleteRoute(id: Int) {
        val client = OkHttpClient()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url("https://testapi20230527192318.azurewebsites.net/api/routes/${id}")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (response.isSuccessful) {
                    _routes.value = _routes.value.filter { it.id != id }
                }
            }
        })
    }
}

