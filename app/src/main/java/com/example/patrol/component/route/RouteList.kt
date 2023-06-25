package com.example.patrol.component.route

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patrol.PointListActivity
import com.example.patrol.R
import com.example.patrol.component.ItemCard
import com.example.patrol.model.Route
import com.example.patrol.viewModel.RouteViewModel

@Composable
fun RouteList(
    hideTools: Boolean = false,
    routes: List<Route>,
    onClick: (route: Route) -> Unit,
    onEdit: (route: Route) -> Unit,
    onDelete: (route: Route) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(routes) { route ->
            ItemCard(
                hideTools = hideTools,
                onEdit = { onEdit(route) },
                onClick = { onClick(route) },
                onDelete = { onDelete(route) }) {
                Text(text = route.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(painterResource(id = R.drawable.outline_place_24), contentDescription = "Image", modifier = Modifier.size(16.dp))
                    Text(text = "${route.points.size}", fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
                }
            }
        }
    }
}