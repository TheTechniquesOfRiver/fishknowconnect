package com.example.fishknowconnect.ui.newPost

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme

class NewPostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout()
                        NewPostScreen("New Post")
                    }
                }
            }
        }
    }
}

/**
 *tool bar design set up
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarLayout() {
    val activity = (LocalContext.current as? Activity)
    CenterAlignedTopAppBar(title = {
        Text(text = "New post")
    }, navigationIcon = {
        IconButton(onClick = {
            activity?.finish()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
        }
    })
}

@Composable
fun NewPostScreen(name: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(all = 8.dp),
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { postText -> },
            label = { Text(text = stringResource(R.string.textview_text_post)) },
            minLines = 5
        )
        Row(
            modifier = Modifier.padding(all = 8.dp),
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Face, contentDescription = "Audio")
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "Images")
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "Video")
            }
        }
        Button(onClick = {}) {
            Icon(imageVector = Icons.Outlined.Done, contentDescription = "Upload")
            Text(
                text = stringResource(id = R.string.button_upload), Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToolBarLayoutPreview() {
    FishKnowConnectTheme {
        ToolBarLayout()
    }
}

@Preview(showBackground = true)
@Composable
fun NewPostScreenPreview() {
    FishKnowConnectTheme {
        NewPostScreen("New Post")
    }
}