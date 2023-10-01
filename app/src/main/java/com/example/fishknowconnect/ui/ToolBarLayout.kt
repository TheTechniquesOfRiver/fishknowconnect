package com.example.fishknowconnect.ui

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

/**
 *tool bar design set up
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarLayout(title: String) {
    val activity = (LocalContext.current as? Activity)
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF70ced3)), title = {
        Text(text = title,
            style = TextStyle(color = Color.White, fontSize = 22.sp)
        )
    }, navigationIcon = {
        IconButton(onClick = {
            activity?.finish()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu", tint = Color.White)
        }
    })
}
