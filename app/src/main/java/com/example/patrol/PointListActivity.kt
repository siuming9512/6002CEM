package com.example.patrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.ItemCard
import com.example.patrol.viewModel.PointViewModel
import kotlinx.coroutines.launch

class PointListActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
//        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "OnCreate")


        setContent {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val activity = context as Activity
            val intent = activity?.intent
            val routeId = intent?.getExtras()?.getInt("routeId")

            var pointViewModel: PointViewModel = viewModel()
            pointViewModel.fetchPoints(routeId!!)
            var points = pointViewModel.points.collectAsState()

            var resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val id = data?.getIntExtra("id", 0)
                    val name = data?.getStringExtra("name")
                    val tagId = data?.getStringExtra("tagId")
                    val lat = data?.getDoubleExtra("lat", 0.0)
                    val long = data?.getDoubleExtra("long", 0.0)

                    if(id == 0) {
                        pointViewModel.addPoint(name!!, tagId!!, routeId, lat!!, long!!)
                    } else {
                        pointViewModel.editPoint(id!!, name!!, tagId!!,  lat!!, long!!)
                    }
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Point",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            var intent = Intent(this, PointCreateFormActivity::class.java)
                            resultLauncher.launch(intent)
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            ) { contentPadding ->
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(points.value) { point ->
                        ItemCard(
                            onClick = {

                            },
                            onEdit = {
                                var intent = Intent(context, PointCreateFormActivity::class.java).apply {
                                    putExtra("id", point.id)
                                    putExtra("name", point.name)
                                    putExtra("tagId", point.tagId)
                                    putExtra("lat", point.latitude)
                                    putExtra("long", point.longitude)
                                }
                                resultLauncher.launch(intent)
                            },
                            onDelete = {
                                pointViewModel.deletePoint(point.id)
                            }) {
                            Text(text = point.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                Icon(painterResource(id = R.drawable.outline_nfc_24), contentDescription = "Image", modifier = Modifier.size(16.dp))
                                Text(text = "${point.tagId}", fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
