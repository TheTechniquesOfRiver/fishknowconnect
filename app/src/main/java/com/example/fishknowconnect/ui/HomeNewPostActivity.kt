package com.example.fishknowconnect.ui

import LocaleHelper
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.newPost.NewPostState
import com.example.fishknowconnect.ui.newPost.NewPostViewModel
import com.example.fishknowconnect.ui.newPost.NewPostViewModelFactory
import com.example.fishknowconnect.ui.newPost.ShowAudioPlayer
import com.example.fishknowconnect.ui.newPost.ShowVideoPlayer
import com.example.fishknowconnect.ui.newPost.createImageFile
import com.example.fishknowconnect.ui.newPost.createVideoFile
import com.example.fishknowconnect.ui.newPost.ui.theme.DrawScrollableView
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.ui.theme.iconPhotoCamera
import com.example.fishknowconnect.ui.newPost.ui.theme.iconRecordVoiceOver
import com.example.fishknowconnect.ui.newPost.ui.theme.iconUpload
import com.example.fishknowconnect.ui.newPost.ui.theme.iconVideoCameraBack
import com.example.fishknowconnect.ui.recordVoice.RecordVoiceActivity
import java.io.File
import java.util.Objects

class HomeNewPostActivity : ComponentActivity() {
    lateinit var newPostViewModelFactory: NewPostViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        newPostViewModelFactory = NewPostViewModelFactory(
            PreferenceHelper.getInstance(applicationContext), FishKnowConnectApi.retrofitService
        )
        val viewModel: NewPostViewModel by viewModels(factoryProducer = { newPostViewModelFactory })

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
     * locale attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    /**
     * shows error dialog
     */
    private fun showDialog() {
        Log.d("result", "error")
    }

    @Composable
    fun NewPostScreen(viewModel: NewPostViewModel) {
        val context = LocalContext.current
        val languageOptions = listOf(
            stringResource(R.string.text_radio_private), stringResource(R.string.text_radio_public)
        )
        val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
        val intentRecordFile = intent.getStringExtra("recordFile")

        //get image file
        val imageFile = context.createImageFile()
        //get video file
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
        var audioVisibility by remember {
            mutableStateOf(false)
        }

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                capturedImageUri = imageUri
                imageVisibility = true
                videoVisibility = false
                audioVisibility = false
                viewModel.updateFileType("image")
                viewModel.updateFile(imageFile)
            }
        val videoLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
                capturedVideoUri = videoUri
                audioVisibility = false
                imageVisibility = false
                videoVisibility = true
                viewModel.updateFileType("video")
                viewModel.updateFile(videoFile)
            }
        if (intentRecordFile != null) {
            val file = File(intentRecordFile)
            audioVisibility = true
            imageVisibility = false
            videoVisibility = false
            viewModel.updateFileType("audio")
            viewModel.updateFile(file)
            if (audioVisibility) {
                ShowAudioPlayer(intentRecordFile)
            }
        }
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
            ShowDropDown(viewModel)
            OutlinedTextField(
                value = viewModel.postTitle,
                modifier = Modifier.padding(all = 16.dp) .fillMaxWidth(),
                onValueChange = { title -> viewModel.updateTitle(title) },
                label = { Text(text = stringResource(R.string.textview_post_title)) },
                minLines = 2
            )
            OutlinedTextField(
                value = viewModel.content,
                modifier = Modifier.padding(all = 16.dp) .fillMaxWidth(),
                onValueChange = { content -> viewModel.updateContent(content) },
                label = { Text(text = stringResource(R.string.textview_text_post)) },
                minLines = 5
            )
            Text(
                text = stringResource(id = R.string.text_who_can_see), style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ), modifier = Modifier.padding(horizontal = 10.dp, 7.dp)
            )
            languageOptions.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .selectable(
                            selected = (text == selectedOption), onClick = {
                                onOptionSelected(text)
                                //set access
                                if (text == "Share privately") {
                                    viewModel.updateAccess("private")
                                } else {
                                    viewModel.updateAccess("public")
                                }
                            }, role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (text == selectedOption), onClick = null,
                    )
                    Log.d("new post", "selectedOption$selectedOption")
                    Text(
                        text = text, style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ), modifier = Modifier.padding(horizontal = 10.dp, 7.dp)
                    )
                }
            }
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

    @Composable
    private fun ShowDropDown(viewModel: NewPostViewModel) {
        var mExpanded by remember { mutableStateOf(false) }
        val types = listOf(
            getString(R.string.textview_fish),
            getString(R.string.textview_weather),
            getString(R.string.textview_water),
            getString(R.string.textview_boat),
            getString(R.string.textview_others)
        )
        var mSelectedText by remember { mutableStateOf("") }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
        // Up Icon when expanded and down icon when collapsed
        val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp
        else Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(value = mSelectedText,
                onValueChange = { mSelectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Type") },
                trailingIcon = {
                    Icon(icon, "contentDescription", Modifier.clickable { mExpanded = !mExpanded })
                })

            // Create a drop-down menu with list of cities,
            // when clicked, set the Text Field text as the city selected
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
            ) {
                types.forEach { label ->
                    DropdownMenuItem(text = { Text(text = label) }, onClick = {
                        mSelectedText = label
                        mExpanded = false
                        viewModel.type(label)
                    })

                }
            }
        }
    }

    /**
     * Open record voice screen
     */
    private fun recordVoice() {
        val intent = Intent(this, RecordVoiceActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun ToolBarLayoutPreview() {
        FishKnowConnectTheme {
            ToolBarLayout("New post")
        }
    }
}

