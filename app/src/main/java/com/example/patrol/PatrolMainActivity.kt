package com.example.patrol

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.patrol.component.ItemCard
import com.example.patrol.component.route.RouteList
import com.example.patrol.model.Route
import com.example.patrol.viewModel.RouteViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request


class PatrolMainActivity : ComponentActivity() {
    //Intialize attributes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val pref = getSharedPreferences("User", ComponentActivity.MODE_PRIVATE)
            val context = LocalContext.current

            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val taskIntent = Intent(context, RouteTaskActivity::class.java)
                        context.startActivity(taskIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 10.dp)
                ) {
                    Icon(painterResource(id = R.drawable.outline_directions_walk_24), contentDescription = "User icon")
                    Text(text = "Patrol", fontSize = MaterialTheme.typography.h6.fontSize)
                }

                Button(
                    onClick = {
                        val taskIntent = Intent(context, RouteListActivity::class.java)
                        context.startActivity(taskIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 10.dp)
                ) {
                    Icon(painterResource(id = R.drawable.outline_settings_24), contentDescription = "User icon")
                    Text(text = "Route Setup", fontSize = MaterialTheme.typography.h6.fontSize)
                }

                Button(
                    onClick = {
                        pref.edit().remove("name").commit()
                        val taskIntent = Intent(context, LoginActivity::class.java)
                        context.startActivity(taskIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 10.dp)
                ) {
                    Icon(painterResource(id = R.drawable.outline_logout_24), contentDescription = "User icon")
                    Text(text = "Logout", fontSize = MaterialTheme.typography.h6.fontSize)
                }
            }
        }
    }
}
