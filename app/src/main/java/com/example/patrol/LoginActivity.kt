package com.example.patrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.patrol.model.Route
import com.example.patrol.model.User
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class LoginActivity : ComponentActivity() {
    //Intialize attributes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("User", MODE_PRIVATE)
        pref.getString("name", null)?.let {
            val intent = Intent(this, PatrolMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContent {

            val context = LocalContext.current
            val composableScope = rememberCoroutineScope()

            val usernameState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
                modifier = Modifier.fillMaxSize(),
            ) { _ ->
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = usernameState.value,
                        onValueChange = { usernameState.value = it },
                        label = { Text("Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    TextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    Button(
                        onClick = {
                            val mediaType = "application/json; charset=utf-8".toMediaType()
                            val jsonObject = JSONObject().apply {
                                put("username", usernameState.value)
                                put("password", passwordState.value)
                            }
                            val client = OkHttpClient()
                            val request = Request.Builder()
                                .url("https://testapi20230527192318.azurewebsites.net/api/users/login")
                                .post(jsonObject.toString().toRequestBody(mediaType))
                                .build()

                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    if(response.isSuccessful) {
                                        val json = response.body?.string()
                                        val user = Gson().fromJson(json, User::class.java)

                                        val pref = getSharedPreferences("User", MODE_PRIVATE)
                                        pref.edit()
                                            .putString("name", user.name)
                                            .commit()

                                        val taskIntent = Intent(context, PatrolMainActivity::class.java)
                                        context.startActivity(taskIntent)
                                    }
                                }
                            })
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Login")
                    }

                    Button(
                        onClick = {
                            val taskIntent = Intent(context, RegisterActivity::class.java)
                            context.startActivity(taskIntent)
                        },
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }
}