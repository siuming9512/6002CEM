package com.example.patrol.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patrol.NfcActivity
import com.example.patrol.R
import com.example.patrol.TaskLogActivity
import com.example.patrol.model.Point
import com.example.patrol.model.PointTaskLog
import com.example.patrol.model.TaskLog
import java.sql.Date
import java.text.SimpleDateFormat

@Composable
fun TaskLogCard(
    taskLog: PointTaskLog,
    onClick: () -> Unit
) {
//    Column(
//        modifier = Modifier
//            .padding(bottom = 16.dp)
//            .clickable(
//                onClick = onClick)
//    ) {
//
//        Text(text = taskLog.pointName)
//        Text(text = taskLog.checked.toString() )
//        Text(text = taskLog.checkedTime?.toString() ?: "")
//        Text(text = taskLog.username ?: "")
//    }
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp)
            .clickable(
                onClick = onClick
            )
    ) {
        Column(
            Modifier.fillMaxHeight()
        ) {
            Text(text = taskLog.point.name, fontSize = 18.sp, modifier = Modifier.padding(start = 2.dp))
            if(taskLog.taskLogId != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(painterResource(id = R.drawable.outline_image_24), contentDescription = "Image", modifier = Modifier.size(18.dp))
                    Text(text = "${taskLog.attachmentCount?: 0}", fontSize = 16.sp)
                }
            }
        }

        if(taskLog.taskLogId == null) {
            Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(36.dp), tint = Color.Red)
        } else {
            Column(
                Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(Icons.Default.Check, contentDescription = "Checked", modifier = Modifier.size(24.dp), tint = Color(0xFF12965d))
                    Text(text = "${formatter.format(taskLog.checkedTime)}", fontSize = 16.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskLogCardPreview() {
    TaskLogCard(
        taskLog = PointTaskLog(
            id = 1,
            taskLogId = 1,
            point = Point(1, "Point 1", null, null, null, "Tag 1", Date.valueOf("2023-06-16")),
            checked = true,
            checkedTime = Date.valueOf("2021-01-01"),
            username = "User 1",
            attachmentCount =  3),
        onClick = {}
    )
}