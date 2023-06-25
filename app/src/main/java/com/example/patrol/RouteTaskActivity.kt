package com.example.patrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.TaskCard
import com.example.patrol.component.drawer.NavFrame
import com.example.patrol.viewModel.RouteTaskViewModel

class RouteTaskActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

//        Log.i("RouteCreateFormActivity", intent.getStringExtra("routeName") ?: "No route name")

        setContent {
            var context = LocalContext.current
            var routeTaskViewModel: RouteTaskViewModel = viewModel()
            var routeTasks = routeTaskViewModel.routeTasks.collectAsState()

            var resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val routeId = data?.getIntExtra("routeId", 0)

                    if(routeId!! > 0) {
                        routeTaskViewModel.addRouteTask(routeId!!, 1)
                    }
                }
            }
            NavFrame(
                title = "Route Task",
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            var addTaskIntent = Intent(this, RouteTaskSelectActiviity::class.java)

                            resultLauncher.launch(addTaskIntent)
                        },
                        ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    }
                }) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(routeTasks.value) { routeTask ->

                        TaskCard(routeTask = routeTask, onClick = {
                            var taskLogIntent =
                                Intent(context, TaskLogListActivity::class.java)
                            taskLogIntent.putExtra("taskId", routeTask.id)
                            context.startActivity(taskLogIntent)
                        })
//                        Column(
//                            modifier = Modifier
//                                .padding(bottom = 16.dp)
//                                .clickable(
//                                    onClick = {
//                                        var taskLogIntent =
//                                            Intent(context, TaskLogListActivity::class.java)
//                                        taskLogIntent.putExtra("taskId", routeTask.id)
//                                        context.startActivity(taskLogIntent)
//                                    })
//                        ) {
//                            Row {
//                                Column {
//                                    Text(text = routeTask.patrolRoute.name, fontSize = 24.sp)
//                                    Text(text = "Assigned To: ${routeTask.user.name}", fontSize = 16.sp)
//                                    Row {
////                                        Text(text = "Checked points: ${routeTask.patr}")
//
//                                    }
//                                }
//                            }
//                            Row {
//                                Row {
//                                    Icon(
//                                        Icons.Default.Clear,
//                                        contentDescription = "Clear icon",
//                                        modifier = Modifier.size(24.dp),
//                                        tint = Color.Red
//                                    )
//                                    Text(text = "${routeTask.patrolRoute.points.size.toString()}")
//                                }
//                                Row {
//                                    Icon(
//                                        Icons.Default.Check,
//                                        contentDescription = "Tick icon",
//                                        modifier = Modifier.size(24.dp),
//                                        tint = Color.Green
//                                    )
//                                    Text(text = "${routeTask.patrolRoute.points.size.toString()}")
//                                }
//                            }
//                            Text(text = routeTask.createDate.toString())
//                        }
                    }
                }
            }
        }
    }
}