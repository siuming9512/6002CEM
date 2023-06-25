package com.example.patrol.component.drawer

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@Composable
fun NavFrame(
    title: String,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        floatingActionButton = {
            floatingActionButton()
        }
    ) {
        content()
    }
}