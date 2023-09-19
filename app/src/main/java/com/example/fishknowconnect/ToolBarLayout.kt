package com.example.fishknowconnect

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 *tool bar design set up
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarLayout(title: String) {
    val activity = (LocalContext.current as? Activity)
    CenterAlignedTopAppBar(title = {
        Text(text = title)
    }, navigationIcon = {
        IconButton(onClick = {
            activity?.finish()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
        }
    })
}