package com.example.fishknowconnect.ui.newPost

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.newPost.ui.theme.DrawScrollableView
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.recordVoice.RecordVoiceActivity
import java.io.File
import java.util.Objects

class NewPostActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: NewPostViewModel by viewModels()
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
                            ToolBarLayout(resources.getString(R.string.text_new_post))
//                            Image(
//                                painter = painterResource(id = R.drawable.dog),
//                                contentDescription = stringResource(id = R.string.text_new_post)
//
//                            )
                            NewPostScreen(viewModel = viewModel)
                            when (val response = viewModel.state.collectAsState().value) {
                                NewPostState.Loading -> IndeterminateCircularIndicator()
                                is NewPostState.Success -> ShowSuccessMessage(response.message)
                                is NewPostState.Error -> showDialog()
                                else -> {
                                }
                            }
                        }
                    }
                })
            }
        }
    }


    /**
     * show success message and close activity
     */
    @Composable
    private fun ShowSuccessMessage(message: String) {
        val activity = (LocalContext.current as? Activity)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }


    /**
     * shows error dialog
     */
    fun showDialog() {
        Log.d("result", "error")
        //TODO show error dialog
    }

    @Composable
    fun NewPostScreen(viewModel: NewPostViewModel) {
        val context = LocalContext.current
        val intentRecordFile = intent.getStringExtra("recordFile")
        Log.d("result", "intentRecordFile$intentRecordFile")


//        val mMediaPlayer = MediaPlayer.create(context, intentRecordFile)
//        Row {
//            // IconButton for Start Action
//            IconButton(onClick = { mMediaPlayer.start() }) {
//                Icon(painter = painterResource(id = R.drawable.video), contentDescription = "", Modifier.size(100.dp))
//            }
//
//            // IconButton for Pause Action
//            IconButton(onClick = { mMediaPlayer.pause() }) {
//                Icon(painter = painterResource(id = R.drawable.video), contentDescription = "", Modifier.size(100.dp))
//            }
//        }
        val imageFile = context.createImageFile()
        val videoFile = context.createVideoFile()
        //gets file uri
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
        var imageVisibility by remember {
            mutableStateOf(false)
        }

        var videoVisibility by remember {
            mutableStateOf(false)
        }
        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                capturedImageUri = imageUri
                imageVisibility = true
                videoVisibility = false
                viewModel.updateFile(imageFile)
            }
        val videoLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
                capturedVideoUri = videoUri
                imageVisibility = false
                videoVisibility = true
                viewModel.updateFile(videoFile)
            }
//        if(!imageFile.exists() || !videoFile.exists()){
//            viewModel.updateFile(File(""))
//        }
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission Accepted:
                cameraLauncher.launch(imageUri)
                Log.d("NEW POST SCREEN", "PERMISSION GRANTED")
            } else {
                // Permission Denied:
                Log.d("NEW POST SCREEN", "PERMISSION DENIED")
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            if (imageVisibility && capturedImageUri.path?.isNotEmpty() == true) {

                Image(
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .height(240.dp),
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentDescription = null
                )

            }
            if (videoVisibility && capturedVideoUri.path?.isNotEmpty() == true) {
                ShowVideoPlayer(videoUri = capturedVideoUri)
            }
            OutlinedTextField(
                value = viewModel.content,
                modifier = Modifier.padding(all = 16.dp),
                onValueChange = { content -> viewModel.updateContent(content) },
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
                    Icon(imageVector = iconRecordVoiceOver(), contentDescription = "Audio")
                }
                IconButton(onClick = {
                    // Check permission
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context, (Manifest.permission.CAMERA)
                        ) -> {
                            // Launch camera
                            cameraLauncher.launch(imageUri)
                        }

                        else -> {
                            // Asking for permission
                            launcher.launch((Manifest.permission.CAMERA))
                        }
                    }
                }) {
                    Icon(imageVector = iconPhotoCamera(), contentDescription = "Images")
                }
                IconButton(onClick = {
                    Toast.makeText(context, "Capture video", Toast.LENGTH_SHORT).show()
                    // Check permission
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context, (Manifest.permission.CAMERA)
                        ) -> {
                            // Launch camera
                            videoLauncher.launch(videoUri)
                        }

                        else -> {
                            // Asking for permission
                            launcher.launch((Manifest.permission.CAMERA))
                        }
                    }
                }) {
                    Icon(imageVector = iconVideoCameraBack(), contentDescription = "Video")
                }
            }
            Button(onClick = {
                val intentType = intent.getStringExtra("type")
                if (intentType != null) {
                    viewModel.type(intentType)
                }
                viewModel.uploadPictureToServer("")
            }) {
                Icon(imageVector = iconUpload(), contentDescription = "Upload")
                Text(
                    text = stringResource(id = R.string.button_upload),
                    Modifier.padding(start = 10.dp),
                    style = TextStyle(
                        fontSize = 20.sp, fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }
    }


    private fun recordVoice() {
//        val intent = Intent(this, RecordVoiceActivity::class.java)
//        startActivity(intent)
    }

    @Composable
    fun iconRecordVoiceOver(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "settings_voice",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(13.208f, 39.833f)
                    quadToRelative(-0.583f, 0f, -0.958f, -0.395f)
                    quadToRelative(-0.375f, -0.396f, -0.375f, -0.98f)
                    quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                    reflectiveQuadToRelative(0.958f, -0.375f)
                    quadToRelative(0.584f, 0f, 0.98f, 0.375f)
                    quadToRelative(0.395f, 0.375f, 0.395f, 0.958f)
                    quadToRelative(0f, 0.584f, -0.395f, 0.98f)
                    quadToRelative(-0.396f, 0.395f, -0.98f, 0.395f)
                    close()
                    moveToRelative(6.792f, 0f)
                    quadToRelative(-0.583f, 0f, -0.958f, -0.395f)
                    quadToRelative(-0.375f, -0.396f, -0.375f, -0.98f)
                    quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                    reflectiveQuadToRelative(0.958f, -0.396f)
                    quadToRelative(0.583f, 0f, 0.979f, 0.375f)
                    reflectiveQuadToRelative(0.396f, 0.958f)
                    quadToRelative(0f, 0.584f, -0.396f, 0.98f)
                    quadToRelative(-0.396f, 0.395f, -0.979f, 0.395f)
                    close()
                    moveToRelative(6.792f, 0f)
                    quadToRelative(-0.584f, 0f, -0.959f, -0.395f)
                    quadToRelative(-0.375f, -0.396f, -0.375f, -0.98f)
                    quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                    reflectiveQuadToRelative(0.959f, -0.375f)
                    quadToRelative(0.583f, 0f, 0.979f, 0.375f)
                    reflectiveQuadToRelative(0.396f, 0.958f)
                    quadToRelative(0f, 0.584f, -0.396f, 0.98f)
                    quadToRelative(-0.396f, 0.395f, -0.979f, 0.395f)
                    close()
                    moveTo(20f, 22.625f)
                    quadToRelative(-1.833f, 0f, -3.062f, -1.313f)
                    quadToRelative(-1.23f, -1.312f, -1.23f, -3.187f)
                    verticalLineTo(7.958f)
                    quadToRelative(0f, -1.791f, 1.25f, -3.041f)
                    reflectiveQuadTo(20f, 3.667f)
                    quadToRelative(1.792f, 0f, 3.042f, 1.25f)
                    quadToRelative(1.25f, 1.25f, 1.25f, 3.041f)
                    verticalLineToRelative(10.167f)
                    quadToRelative(0f, 1.875f, -1.23f, 3.187f)
                    quadToRelative(-1.229f, 1.313f, -3.062f, 1.313f)
                    close()
                    moveToRelative(0f, -9.5f)
                    close()
                    moveToRelative(0f, 21.708f)
                    quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                    reflectiveQuadToRelative(-0.375f, -0.958f)
                    verticalLineToRelative(-4.125f)
                    quadToRelative(-4f, -0.458f, -6.791f, -3.208f)
                    quadToRelative(-2.792f, -2.75f, -3.25f, -6.625f)
                    quadToRelative(-0.084f, -0.584f, 0.312f, -1f)
                    quadToRelative(0.396f, -0.417f, 1.021f, -0.417f)
                    quadToRelative(0.458f, 0f, 0.833f, 0.354f)
                    quadToRelative(0.375f, 0.354f, 0.459f, 0.854f)
                    quadToRelative(0.458f, 3.209f, 2.896f, 5.334f)
                    quadToRelative(2.437f, 2.125f, 5.812f, 2.125f)
                    quadToRelative(3.375f, 0f, 5.812f, -2.125f)
                    quadToRelative(2.438f, -2.125f, 2.896f, -5.334f)
                    quadToRelative(0.084f, -0.5f, 0.459f, -0.854f)
                    reflectiveQuadToRelative(0.875f, -0.354f)
                    quadToRelative(0.583f, 0f, 0.979f, 0.417f)
                    quadToRelative(0.396f, 0.416f, 0.312f, 1f)
                    quadToRelative(-0.458f, 3.875f, -3.25f, 6.625f)
                    quadToRelative(-2.791f, 2.75f, -6.75f, 3.208f)
                    verticalLineTo(33.5f)
                    quadToRelative(0f, 0.583f, -0.395f, 0.958f)
                    quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                    close()
                    moveToRelative(0f, -14.875f)
                    quadToRelative(0.75f, 0f, 1.208f, -0.52f)
                    quadToRelative(0.459f, -0.521f, 0.459f, -1.313f)
                    verticalLineTo(7.958f)
                    quadToRelative(0f, -0.708f, -0.479f, -1.187f)
                    quadToRelative(-0.48f, -0.479f, -1.188f, -0.479f)
                    reflectiveQuadToRelative(-1.188f, 0.479f)
                    quadToRelative(-0.479f, 0.479f, -0.479f, 1.187f)
                    verticalLineToRelative(10.167f)
                    quadToRelative(0f, 0.792f, 0.459f, 1.313f)
                    quadToRelative(0.458f, 0.52f, 1.208f, 0.52f)
                    close()
                }
            }.build()

        }
    }

    @Composable
    fun iconPhotoCamera(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "photo_camera",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(20f, 28.792f)
                    quadToRelative(3f, 0f, 5.021f, -2.021f)
                    reflectiveQuadToRelative(2.021f, -5.021f)
                    quadToRelative(0f, -3f, -2.021f, -5.021f)
                    reflectiveQuadTo(20f, 14.708f)
                    quadToRelative(-3f, 0f, -5.021f, 2.021f)
                    reflectiveQuadToRelative(-2.021f, 5.021f)
                    quadToRelative(0f, 3f, 2.021f, 5.021f)
                    reflectiveQuadTo(20f, 28.792f)
                    close()
                    moveTo(6.25f, 34.75f)
                    quadToRelative(-1.083f, 0f, -1.854f, -0.792f)
                    quadToRelative(-0.771f, -0.791f, -0.771f, -1.833f)
                    verticalLineToRelative(-20.75f)
                    quadToRelative(0f, -1.042f, 0.771f, -1.833f)
                    quadToRelative(0.771f, -0.792f, 1.854f, -0.792f)
                    horizontalLineToRelative(5.833f)
                    lineToRelative(2.209f, -2.625f)
                    quadToRelative(0.375f, -0.417f, 0.896f, -0.646f)
                    quadToRelative(0.52f, -0.229f, 1.104f, -0.229f)
                    horizontalLineToRelative(7.416f)
                    quadToRelative(0.584f, 0f, 1.104f, 0.229f)
                    quadToRelative(0.521f, 0.229f, 0.896f, 0.646f)
                    lineToRelative(2.209f, 2.625f)
                    horizontalLineToRelative(5.833f)
                    quadToRelative(1.042f, 0f, 1.833f, 0.792f)
                    quadToRelative(0.792f, 0.791f, 0.792f, 1.833f)
                    verticalLineToRelative(20.75f)
                    quadToRelative(0f, 1.042f, -0.792f, 1.833f)
                    quadToRelative(-0.791f, 0.792f, -1.833f, 0.792f)
                    close()
                    moveToRelative(27.5f, -2.625f)
                    verticalLineToRelative(-20.75f)
                    horizontalLineTo(6.25f)
                    verticalLineToRelative(20.75f)
                    close()
                    moveTo(20f, 21.75f)
                    close()
                }
            }.build()
        }
    }

    @Composable
    fun iconVideoCameraBack(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "video_camera_back",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(6.292f, 33.083f)
                    quadToRelative(-1.042f, 0f, -1.834f, -0.791f)
                    quadToRelative(-0.791f, -0.792f, -0.791f, -1.834f)
                    verticalLineTo(9.542f)
                    quadToRelative(0f, -1.042f, 0.791f, -1.834f)
                    quadToRelative(0.792f, -0.791f, 1.834f, -0.791f)
                    horizontalLineToRelative(20.916f)
                    quadToRelative(1.042f, 0f, 1.834f, 0.791f)
                    quadToRelative(0.791f, 0.792f, 0.791f, 1.834f)
                    verticalLineToRelative(8.5f)
                    lineToRelative(5.375f, -5.375f)
                    quadToRelative(0.292f, -0.292f, 0.73f, -0.146f)
                    quadToRelative(0.437f, 0.146f, 0.437f, 0.604f)
                    verticalLineToRelative(13.75f)
                    quadToRelative(0f, 0.458f, -0.437f, 0.604f)
                    quadToRelative(-0.438f, 0.146f, -0.73f, -0.187f)
                    lineToRelative(-5.375f, -5.334f)
                    verticalLineToRelative(8.5f)
                    quadToRelative(0f, 1.042f, -0.791f, 1.834f)
                    quadToRelative(-0.792f, 0.791f, -1.834f, 0.791f)
                    close()
                    moveToRelative(0f, -2.625f)
                    horizontalLineToRelative(20.916f)
                    verticalLineTo(9.542f)
                    horizontalLineTo(6.292f)
                    verticalLineToRelative(20.916f)
                    close()
                    moveToRelative(3.75f, -4.25f)
                    horizontalLineTo(23.75f)
                    quadToRelative(0.375f, 0f, 0.562f, -0.354f)
                    quadToRelative(0.188f, -0.354f, -0.062f, -0.729f)
                    lineToRelative(-4.042f, -5.375f)
                    quadToRelative(-0.208f, -0.292f, -0.52f, -0.292f)
                    quadToRelative(-0.313f, 0f, -0.521f, 0.292f)
                    lineToRelative(-3.709f, 4.833f)
                    lineToRelative(-2.25f, -2.916f)
                    quadToRelative(-0.208f, -0.292f, -0.541f, -0.271f)
                    quadToRelative(-0.334f, 0.021f, -0.542f, 0.312f)
                    lineTo(9.5f, 25.167f)
                    quadToRelative(-0.25f, 0.333f, -0.062f, 0.687f)
                    quadToRelative(0.187f, 0.354f, 0.604f, 0.354f)
                    close()
                    moveToRelative(-3.75f, 4.25f)
                    verticalLineTo(9.542f)
                    verticalLineToRelative(20.916f)
                    close()
                }
            }.build()
        }
    }

    @Composable
    fun iconUpload(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "upload",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(20f, 26.792f)
                    quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                    reflectiveQuadToRelative(-0.375f, -0.959f)
                    verticalLineTo(12f)
                    lineToRelative(-3.833f, 3.792f)
                    quadToRelative(-0.417f, 0.375f, -0.937f, 0.375f)
                    quadToRelative(-0.521f, 0f, -0.896f, -0.375f)
                    quadToRelative(-0.375f, -0.417f, -0.375f, -0.959f)
                    quadToRelative(0f, -0.541f, 0.375f, -0.916f)
                    lineToRelative(6.041f, -6.042f)
                    quadToRelative(0.209f, -0.208f, 0.438f, -0.292f)
                    quadTo(19.75f, 7.5f, 20f, 7.5f)
                    quadToRelative(0.25f, 0f, 0.479f, 0.083f)
                    quadToRelative(0.229f, 0.084f, 0.438f, 0.292f)
                    lineTo(27f, 13.958f)
                    quadToRelative(0.375f, 0.375f, 0.375f, 0.896f)
                    reflectiveQuadToRelative(-0.375f, 0.938f)
                    quadToRelative(-0.417f, 0.375f, -0.958f, 0.375f)
                    quadToRelative(-0.542f, 0f, -0.917f, -0.417f)
                    lineTo(21.333f, 12f)
                    verticalLineToRelative(13.458f)
                    quadToRelative(0f, 0.584f, -0.395f, 0.959f)
                    quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                    close()
                    moveTo(9.542f, 32.958f)
                    quadToRelative(-1.042f, 0f, -1.834f, -0.791f)
                    quadToRelative(-0.791f, -0.792f, -0.791f, -1.834f)
                    verticalLineToRelative(-4.291f)
                    quadToRelative(0f, -0.542f, 0.395f, -0.938f)
                    quadToRelative(0.396f, -0.396f, 0.938f, -0.396f)
                    quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                    reflectiveQuadToRelative(0.375f, 0.938f)
                    verticalLineToRelative(4.291f)
                    horizontalLineToRelative(20.916f)
                    verticalLineToRelative(-4.291f)
                    quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                    quadToRelative(0.375f, -0.396f, 0.917f, -0.396f)
                    quadToRelative(0.583f, 0f, 0.958f, 0.396f)
                    reflectiveQuadToRelative(0.375f, 0.938f)
                    verticalLineToRelative(4.291f)
                    quadToRelative(0f, 1.042f, -0.791f, 1.834f)
                    quadToRelative(-0.792f, 0.791f, -1.834f, 0.791f)
                    close()
                }
            }.build()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ToolBarLayoutPreview() {
        FishKnowConnectTheme {
            ToolBarLayout("New post")
        }
    }
}

