package com.example.patrol.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.patrol.R
import com.example.patrol.model.Attachment
import com.example.patrol.model.Point
import com.example.patrol.model.Route
import com.example.patrol.model.RouteTask
import com.example.patrol.model.TaskLog
import com.example.patrol.model.TaskLogAttachment
import com.example.patrol.model.User
import java.sql.Date
import java.text.SimpleDateFormat


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskLogDetail(
    taskLog: TaskLog,
    onClick: () -> Unit) {
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "${taskLog.point.name}",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            })
        },
        modifier = Modifier.fillMaxSize(),
    ) { _ ->
        Column(
            Modifier.padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(id = R.drawable.outline_assignment_ind_24), contentDescription = "User icon",modifier = Modifier.size(30.dp))
                Text(text = " ${taskLog.patrolTask.user.name}", fontSize = 16.sp)
            }

            Row(
                Modifier.padding(top = 5.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(painterResource(id = R.drawable.outline_image_24), contentDescription = "Image", modifier = Modifier.size(30.dp))
                    Text(text = "${taskLog.taskLogAttachments.size}", fontSize = 16.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(Icons.Default.Check, contentDescription = "User icon", modifier = Modifier.size(30.dp), tint = Color(0xFF12965d))
                    Text(text = "${formatter.format(taskLog.createDate)}", fontSize = 16.sp)
                }
            }


            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = onClick,
            ) {
                Text(
                    text = "Take Photo",
                    modifier = Modifier.padding(end = 8.dp),
                )
                Icon(painterResource(
                    id = R.drawable.sharp_photo_camera_24),
                    contentDescription = "Take Photo",
                    modifier = Modifier.size(24.dp))
            }

//                HorizontalPager(pageCount = taskLog.taskLogAttachments.size) { page ->
//                    val url = taskLog.taskLogAttachments.get(page)?.attachment?.imageUrl
//
//                    AsyncImage(
//                        model = url,
//                        contentDescription = null,
//                    )
//                }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(taskLog.taskLogAttachments) { photo ->
                    if(photo.attachment != null) {
                        Image(painter = rememberAsyncImagePainter(photo.attachment.imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskLogDetailPreview() {
    val points = listOf(Point(1, "Point 1", "", null, null, "Tag 1", Date.valueOf("2023-06-16")))
    val route = Route(1, "Route1", "", Date.valueOf("2023-06-16"), points)
    val user = User(1, "user 1", Date.valueOf("2023-06-16"))
    val routeTask = RouteTask(1, Date.valueOf("2023-06-16"), patrolRoute = route, user = user, patrolTaskLogs = emptyList())
    var attachment = Attachment(1, "d60ab4a7-3b01-4a19-959e-856c75237ada.jpeg", "", "https://patrolimage.blob.core.windows.net/patrol-image/d60ab4a7-3b01-4a19-959e-856c75237ada.jpeg", Date.valueOf("2023-06-16"))
    var taskLogAttachments = listOf<TaskLogAttachment>(
        TaskLogAttachment(1, attachment, Date.valueOf("2023-06-16")),
        TaskLogAttachment(2, attachment, Date.valueOf("2023-06-16"))
    )


    TaskLogDetail(taskLog = TaskLog(1, routeTask, points.get(0), taskLogAttachments, Date.valueOf("2023-06-16")), onClick = {})
}