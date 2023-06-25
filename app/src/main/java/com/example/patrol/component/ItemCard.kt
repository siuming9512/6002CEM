package com.example.patrol.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemCard(
    onEdit: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    hideTools: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(
                    top = 10.dp,
                    bottom = 10.dp
                )
                .clickable {
                    onClick()
                }
        ) {
            Column{
                content()
            }

            if(!hideTools) {
                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()) {
                    IconButton(
                        onClick = {
                            onEdit()
                        },
                        modifier = Modifier
                            .padding(end = 5.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .size(28.dp),
                            tint = Color(0xff1677ff)
                        )
                    }
                    IconButton(
                        onClick = {
                            onDelete()
                        },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(28.dp),
                            tint = Color(0xffff4d4f)
                        )
                    }
                }
            }
        }

        Divider(
//            modifier = Modifier.padding(vertical = 8.dp),
            color = Color(0xffefefef) ,
            thickness = 1.dp
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    ItemCard(onClick = {}, onEdit = {}, onDelete = {}) {
        Text(text = "123123", fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Text(text = "123123")
        Text(text = "123123")
    }
}