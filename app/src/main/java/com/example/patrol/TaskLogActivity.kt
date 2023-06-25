package com.example.patrol

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.component.TaskLogDetail
import com.example.patrol.viewModel.TaskLogViewModel
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


class TaskLogActivity: ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val activity = context as Activity
            val intent = activity?.intent
            val taskLogId = intent?.getExtras()?.getInt("taskLogId")

            var taskLogViewModel: TaskLogViewModel = viewModel()
            taskLogViewModel.fetchTaskLog(taskLogId!!)
            var taskLog = taskLogViewModel.taskLog.collectAsState()

            val file = File(filesDir, "my_images")
            val uri =
                FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)


//            val file = context.createImageFile()
//            val uri = FileProvider.getUriForFile(
//                Objects.requireNonNull(context),
//                BuildConfig.APPLICATION_ID + ".provider", file
//            )

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { image ->
                    lifecycleScope.launch {
                        val compressedImageFile = Compressor.compress(context, file)

                        taskLogViewModel.addAttachment(taskLogId, compressedImageFile)
                    }
                },
            )

            if(taskLog.value != null) {
                TaskLogDetail(
                    taskLog = taskLog.value!!,
                    onClick = {
                        cameraLauncher.launch(uri)
                    }
                )
            }
//                    Column() {
//                        Text(text = "Route Name: ${taskLog.value!!.patrolTask.patrolRoute.name}")
//                        Text(text = "Point Name: ${taskLog.value!!.point.name}")
//                        Text(text = "Tag Id: ${taskLog.value!!.point.tagId}")
//                        Text(text = "Patrol Time: ${taskLog.value!!.createDate}")
//                        Text(text = "Patrol Staff: ${taskLog.value!!.patrolTask.user.name}")
//                        Text(text = "Attachments: ${taskLog.value!!.taskLogAttachments.size}")
//
//                        Button(
//                            modifier = Modifier.padding(top = 16.dp),
//                            onClick = {
//                                cameraLauncher.launch(uri)
//                            },
//                        ) {
//                            Text(
//                                text = "Take photo"
//                            )
//                        }
//                        HorizontalPager(pageCount = taskLog.value!!.taskLogAttachments.size) { page ->
//
//                            val url = taskLog.value!!.taskLogAttachments.get(page)?.attachment?.imageUrl
//
//                            AsyncImage(
//                                model = url,
//                                contentDescription = null,
//                            )
//                        }
//
//                    }
        }
    }
}


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

