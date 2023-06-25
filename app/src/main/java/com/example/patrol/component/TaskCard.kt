package com.example.patrol.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.patrol.R
import com.example.patrol.TaskLogListActivity
import com.example.patrol.model.Point
import com.example.patrol.model.Route
import com.example.patrol.model.RouteTask
import com.example.patrol.model.TaskLog
import com.example.patrol.model.User
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@Composable
fun TaskCard(
    routeTask: RouteTask,
    onClick: () -> Unit,
) {
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var points = routeTask.patrolRoute.points.size
    var checkedPoints = routeTask.patrolTaskLogs.size
    var unCheckPoints = points - checkedPoints
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable(
                onClick = onClick
            ).height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = routeTask.patrolRoute.name, fontSize = 18.sp)
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Icon(painterResource(id = R.drawable.outline_assignment_ind_24), contentDescription = "User icon")
                    Text(text = " ${routeTask.user.name}", fontSize = 18.sp)
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row{
                    Row(
                    ) {
                        Text(text = "${unCheckPoints}", fontSize = 20.sp)
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear icon",
                            modifier = Modifier.size(28.dp),
                            tint = Color.Red
                        )
                    }
                    Row(
                        modifier = Modifier.padding(start = 3.dp)
                    ) {
                        Text(text = "${checkedPoints}", fontSize = 20.sp)
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Tick icon",
                            modifier = Modifier.size(28.dp),
                            tint = Color(0xFF12965d)
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = formatter.format(routeTask.createDate), fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TaskCardPreview() {
    val points = listOf(Point(1, "Point 1", "", null, null,"Tag 1", Date.valueOf("2023-06-16")))
    val route = Route(1, "Route1", "", Date.valueOf("2023-06-16"), points)
    val user = User(1, "user 1", Date.valueOf("2023-06-16"))
    val routeTask = RouteTask(1, Date.valueOf("2023-06-16"), patrolRoute = route, user = user, patrolTaskLogs = emptyList())
    TaskCard(routeTask = routeTask) {

    }
}