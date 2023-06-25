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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.drawer.NavFrame
import com.example.patrol.component.route.RouteList
import com.example.patrol.viewModel.RouteViewModel

class RouteListActivity : ComponentActivity() {
    //Intialize attributes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "OnCreate")
//        routes = fetchRoutes()


//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

        setContent {
            ManagmentView()
        }
    }

}


@Preview
@Composable
fun ManagmentView() {
    var context = LocalContext.current
    var routeViewModel: RouteViewModel = viewModel()
    var routes = routeViewModel.routes.collectAsState()


    var resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val id = data?.getIntExtra("id", 0)
            val name = data?.getStringExtra("name")
            val desc = data?.getStringExtra("desc")

            if(id == 0) {
                routeViewModel.addRoute(name!!, desc)
            } else {
                routeViewModel.editRoute(id!!, name!!, desc)
            }
        }
    }

    NavFrame(
        title = "Route",
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    var addRouteIntent = Intent(context, RouteCreateFormActiviity::class.java)

                    resultLauncher.launch(addRouteIntent)
                },

                ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }) {
        RouteList (
            routes = routes.value,
            onClick = {
                var pointsIntent =
                    Intent(context, PointListActivity::class.java).apply {
                        putExtra("routeId", it.id)
                    }

                context.startActivity(pointsIntent)
            },
            onEdit = {
                var intent = Intent(context, RouteCreateFormActiviity::class.java).apply {
                    putExtra("id", it.id)
                    putExtra("name", it.name)
                    putExtra("desc", it.desc)
                }
                resultLauncher.launch(intent)
            },
            onDelete = {
                routeViewModel.deleteRoute(it.id)
            }
        )
    }
}
