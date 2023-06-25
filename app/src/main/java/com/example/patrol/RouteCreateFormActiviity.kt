package com.example.patrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RouteCreateFormActiviity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.i("RouteCreateFormActivity", intent.getStringExtra("routeName") ?: "No route name")

        setContent {
            val idState = remember { mutableStateOf(intent?.getExtras()?.getInt("id")?: 0) }
            val nameState = remember { mutableStateOf(intent?.getExtras()?.getString("name")?:"") }
            val descState = remember { mutableStateOf(intent?.getExtras()?.getString("desc")?:"") }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Add Route",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
                modifier = Modifier.fillMaxSize(),
            ) { _ ->
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = nameState.value,
                        onValueChange = { nameState.value = it },
                        label = { Text("Route Name") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )

                    TextField(
                        value = descState.value,
                        onValueChange = { descState.value = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )

                    Button(
                        onClick = {
                            val data = Intent().apply {
                                putExtra("id", idState.value)
                                putExtra("name", nameState.value)
                                putExtra("tagId", descState.value)
                            }
                            setResult(RESULT_OK, data);
                            finish()
                        },
                        modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}