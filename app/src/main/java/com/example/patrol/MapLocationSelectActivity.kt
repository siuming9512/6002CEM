package com.example.patrol

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


class MapLocationSelectActivity : ComponentActivity() {

    private var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            var currentLocation by remember {
                mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    for (lo in p0.locations) {
                        // Update UI with location data
                        currentLocation = LocationDetails(lo.latitude, lo.longitude)
                    }
                }
            }

            val launcherMultiplePermissions = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsMap ->
                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                if (areGranted) {
                    locationRequired = true
                    startLocationUpdates()
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
            val centerLocation = LatLng(22.3637527,114.1657928)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(centerLocation, 11f)
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Select Location")
                        },
                        actions = {
                            Button(onClick = {
                                val data = Intent().apply {
                                    putExtra("lat", cameraPositionState.position.target.latitude)
                                    putExtra("long", cameraPositionState.position.target.longitude)
                                    putExtra("type", "LOCATION")
                                }
                                setResult(RESULT_OK, data);
                                finish()
                            }) {
                                Text(text = "Select")
                            }
                        }
                    )
                }
            ) { _ ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                    GoogleMap(
//                    modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true),
                        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
                    ) {
                    }

                    Column(modifier = Modifier.padding(bottom = 32.dp)) {
                        Icon(painterResource(id = R.drawable.baseline_location_on_24), contentDescription = "Image", modifier = Modifier.size(48.dp), tint = Color(0xFFEA4335))
                    }
                }
            }


//            Button(onClick = {
//                val permissions = arrayOf(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                if (permissions.all {
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            it
//                        ) == PackageManager.PERMISSION_GRANTED
//                    }) {
//                    // Get the location
//                    startLocationUpdates()
//                } else {
//                    launcherMultiplePermissions.launch(permissions)
//                }
//            }) {
//                Text(text = "Get current location")
//            }


//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colors.background
//            ) {
//
//
//
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//
//
//                    Text(text = "Latitude : " + currentLocation.latitude)
//                    Text(text = "Longitude : " + currentLocation.longitude)
//                }
//
//            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
}

data class LocationDetails(val latitude: Double, val longitude: Double)