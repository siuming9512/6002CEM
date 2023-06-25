package com.example.patrol

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.TaskLogCard
import com.example.patrol.model.TaskLog
import com.example.patrol.viewModel.PointTaskLogViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


class TaskLogListActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = LocalContext.current
            val activity = context as Activity
            val intent = activity?.intent
            val taskId = intent?.getExtras()?.getInt("taskId")

            var pointTaskLogViewModel: PointTaskLogViewModel = viewModel()
            pointTaskLogViewModel.fetchPointTaskLogs(taskId!!)
            var pointTaskLogs = pointTaskLogViewModel.pointTaskLogs.collectAsState()

            val pointId = remember { mutableStateOf<Int?>(null) }

            var resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                try {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        val tagId = data?.getStringExtra("tagId")
                        if(tagId != null) {
                            pointTaskLogViewModel.addTaskLog(taskId!!, pointId.value!!) {
                                if(it != null) {
                                    var taskLogIntent = Intent(context, TaskLogActivity::class.java)

                                    taskLogIntent.putExtra("taskLogId", it.id)
                                    context.startActivity(taskLogIntent)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = "Task Log List",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    })
                },
                floatingActionButton = {
                }
            ) { _ ->
                Column {
                    if(pointTaskLogs.value.size!! > 0) {
                        var medianIndex = getMedianIndex(pointTaskLogs.value)

                        var center = pointTaskLogs.value.get(medianIndex)
                        val centerLocation = LatLng(center.point.latitude!!, center.point.longitude!!)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(centerLocation, 14f)
                        }
                        Column {

                            GoogleMap(
                                modifier = Modifier
                                    .height(250.dp)
                                    .fillMaxWidth(),
                                cameraPositionState = cameraPositionState
                            ) {
                                pointTaskLogs.value.mapIndexed { index, it ->
                                    if(it.point.latitude != null && it.point.longitude != null) {
                                        val marker = Marker(
                                            state = MarkerState(position = LatLng(it.point.latitude, it.point.longitude)),
                                            title = it.point.name,
                                            icon = BitmapDescriptorFactory.fromBitmap(createCircleWithTextBitmap((index + 1).toString(), it.checked))
                                        )

                                        marker
                                    }
                                }
                            }
                        }
                    }

                    Column {
                        LazyColumn(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            items(pointTaskLogs.value) { taskLog ->
                                TaskLogCard(
                                    taskLog = taskLog,
                                    onClick = {
                                        if(taskLog.checked) {
                                            var taskLogIntent = Intent(context, TaskLogActivity::class.java)
                                            taskLogIntent.putExtra("taskLogId", taskLog.taskLogId)
                                            context.startActivity(taskLogIntent)
                                        } else {
                                            val intent = Intent(context, NfcActivity::class.java)

                                            pointId.value = taskLog.point.id
                                            resultLauncher.launch(intent)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

fun goToTaskLogDetail(taskLog: TaskLog) {

}

fun createCircleWithTextBitmap(text: String, checked: Boolean): Bitmap {
    // Create a new Bitmap with the specified diameter
    val bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888)

    // Create a new Canvas to draw on the Bitmap
    val canvas = Canvas(bitmap)

    // Create a Paint object for the circle background

    var circleColor = Color(0xFF12965d)

    if(!checked) {
        circleColor = Color.Red
    }

    val circlePaint = Paint().apply {
        color = circleColor.toArgb()
        isAntiAlias = true
    }

    // Create a Paint object for the text
    val textPaint = Paint().apply {
        color = Color.White.toArgb()
        isAntiAlias = true
        this.textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    // Draw the circle on the canvas
    val centerX = 80 / 2f
    val centerY = 80 / 2f
    canvas.drawCircle(centerX, centerY, 80 / 2f, circlePaint)

    // Draw the text on the canvas
    val textBounds = Rect()
    textPaint.getTextBounds(text, 0, text.length, textBounds)
    val textX = centerX
    val textY = centerY + textBounds.height() / 2f
    canvas.drawText(text, textX, textY, textPaint)

    return bitmap
}

fun getMedianIndex(list: List<Any>): Int {
    val size = list.size
    return if (size % 2 == 0) {
        size / 2 - 1   // average of the two middle indices for even-sized list
    } else {
        size / 2       // middle index for odd-sized list
    }
}