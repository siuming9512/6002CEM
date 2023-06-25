package com.example.patrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.route.RouteList
import com.example.patrol.viewModel.RouteViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RouteTaskSelectActiviity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var routeViewModel: RouteViewModel = viewModel()
            var routes = routeViewModel.routes.collectAsState()

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Select Route",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
                floatingActionButton = {
                }
            ) { _ ->
                RouteList (
                    hideTools = true,
                    routes = routes.value,
                    onClick = {
                        val data = Intent().apply {
                            putExtra("routeId", it.id)
                        }
                        setResult(RESULT_OK, data);
                        finish()
                    },
                    onEdit = {},
                    onDelete = {}
                )
            }
        }
    }
}