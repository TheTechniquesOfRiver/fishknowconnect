package com.example.fishknowconnect.ui.newPost

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.newPost.ui.theme.DrawScrollableView
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import java.util.Objects

class NewPostActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                DrawScrollableView(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), content = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            ToolBarLayout()
                            NewPostScreen("New Post")
                        }
                    }
                })
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
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.primary,), title = {
        Text(text = "New post",
            style = TextStyle(color = Color.White, fontSize = 20.sp))
    },  navigationIcon = {
        IconButton(onClick = {
            activity?.finish()
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Menu")
        }
    })
}


@Composable
fun NewPostScreen(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageFile = context.createImageFile()
    val videoFile = context.createVideoFile()

    val imageUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), context.packageName + ".provider", imageFile
    )
    var videoUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), context.packageName + ".provider", videoFile
    )
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    var capturedVideoUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    var hasImage by remember {
        mutableStateOf(false)
    }
    var hasVideo by remember {
        mutableStateOf(false)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = imageUri
        hasImage = true

    }
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
        capturedVideoUri = videoUri
        hasVideo = true
        Log.d("NEW POST SCREEN", "PERMISSION $videoUri")

    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            hasImage = false
            hasVideo = false

            // Permission Accepted:
            cameraLauncher.launch(imageUri)
            videoLauncher.launch(videoUri)
            Log.d("NEW POST SCREEN", "PERMISSION GRANTED")
        } else {
            // Permission Denied:
            Log.d("NEW POST SCREEN", "PERMISSION DENIED")
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(all = 8.dp),
    ) {
        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .height(240.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        }
        if (capturedVideoUri.path?.isNotEmpty() == true) {
            ShowVideoPlayer(videoUri = capturedVideoUri)
        }
        OutlinedTextField(
            value = "",
            modifier = Modifier.padding(all = 16.dp),
            onValueChange = { postText -> },
            label = { Text(text = stringResource(R.string.textview_text_post)) },
            minLines = 5
        )
        Row(
            modifier = Modifier.padding(all = 8.dp),
        ) {
            IconButton(onClick = {
                recordVoice()
                Toast.makeText(context, "Record voice", Toast.LENGTH_SHORT).show()
            }) {
                Icon(imageVector = Icons.Outlined.Face, contentDescription = "Audio")
            }
            IconButton(onClick = {
                // Check permission
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context, (Manifest.permission.CAMERA)
                    ) -> {
//                        hasImage = false
                        // Launch camera
                        cameraLauncher.launch(imageUri)
                    }

                    else -> {
//                        hasImage = false
                        // Asking for permission
                        launcher.launch((Manifest.permission.CAMERA))
                    }
                }
            }) {
                Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "Images")
            }
            IconButton(onClick = {
                // Check permission
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context, (Manifest.permission.CAMERA)
                    ) -> {
//                        hasVideo = false

                        // Launch camera
                        videoLauncher.launch(videoUri)
                    }

                    else -> {
//                        hasVideo = fase
                        // Asking for permission
                        launcher.launch((Manifest.permission.CAMERA))
                    }
                }
            }) {
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


fun recordVoice() {

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