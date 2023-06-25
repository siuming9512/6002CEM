package com.example.patrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody

class PointCreateFormActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.i("RouteCreateFormActivity", intent.getStringExtra("routeName") ?: "No route name")

        setContent {
            val context = LocalContext.current
            val activity = context as Activity
            val intent = activity?.intent

            val idState = remember { mutableStateOf(intent?.getExtras()?.getInt("id")?: 0) }
            val nameState = remember { mutableStateOf(intent?.getExtras()?.getString("name")?:"") }
            val tagIdState = remember { mutableStateOf(intent?.getExtras()?.getString("tagId")?: "") }
            val latState = remember { mutableStateOf(intent?.getExtras()?.getDouble("lat", 0.0)) }
            val longState = remember { mutableStateOf(intent?.getExtras()?.getDouble("long", 0.0)) }

            var resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                try {
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
                        val type = data?.getStringExtra("type")

                        if(type == "NFC") {
                            val tagId = data?.getStringExtra("tagId")
                            Log.i("RouteCreateFormActivity", "result: $tagId")

                            if(tagId != null) {
                                tagIdState.value = tagId
                            }
                        } else if (type == "LOCATION") {
                            val lat = data?.getDoubleExtra("lat", 0.0)
                            val long = data?.getDoubleExtra("long", 0.0)

                            if(lat!! > 0.0) {
                                latState.value = lat
                            }

                            if(long!! > 0.0) {
                                longState.value = long
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RouteCreateFormActivity", "Error: $e")
                }

            }
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Add Point",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
            ) { _ ->
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = nameState.value,
                        onValueChange = { nameState.value = it },
                        label = { Text("Point Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ){
                        Button(
                            onClick = {
                                val intent = Intent(this@PointCreateFormActivity, NfcActivity::class.java)

                                resultLauncher.launch(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(painterResource(id = R.drawable.outline_nfc_24), contentDescription = "Image")
                            if(tagIdState.value != null && tagIdState.value != "") {
                                Text(text = "${tagIdState.value}", fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ){
                        var label = "";

                        Button(
                            onClick = {
                                val intent = Intent(this@PointCreateFormActivity, MapLocationSelectActivity::class.java)

                                resultLauncher.launch(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row {
                                Icon(painterResource(id = R.drawable.baseline_location_on_24), contentDescription = "Image")

                                if(latState.value != null) {

                                    Column {
                                        Text(text = "${String.format("%.5f", latState.value)}, ${String.format("%.5f", longState.value)}", fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                                    }
                                }
                            }
                        }

                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    ) {
                        Button(
                            onClick = {
                                val data = Intent().apply {
                                    putExtra("id", idState.value)
                                    putExtra("name", nameState.value)
                                    putExtra("tagId", tagIdState.value)
                                    putExtra("lat", latState.value)
                                    putExtra("long", longState.value)
                                }
                                setResult(RESULT_OK, data);
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Submit", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}