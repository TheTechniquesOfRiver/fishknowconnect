package com.example.fishknowconnect.ui.contentDetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.contentDetail.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.ShowAudioPlayer
import com.example.fishknowconnect.ui.newPost.ShowVideoPlayer
import java.util.Locale

var tts: TextToSpeech? = null
var isListenEnable = false

class ContentDetailActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    val viewModel: DeleteContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    // TextToSpeech(Context: this, OnInitListener: this)
                    tts = TextToSpeech(this@ContentDetailActivity, this)
                    Column {
                        ToolBarLayout(resources.getString(R.string.text_content_details))
                        ListItemDetailScreen(intent, viewModel)
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            DeleteContentState.Loading -> IndeterminateCircularIndicator()
                            is DeleteContentState.Success -> responseValue.response?.let {
                                Toast.makeText(
                                    applicationContext,
                                    responseValue.response.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }

                            is DeleteContentState.Error -> ShowDeleteErrorMessage(responseValue.response.message)
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * shows error dialog
     */
    @Composable
    fun ShowDeleteErrorMessage(message: String) {
        val context = LocalContext.current as? Activity
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    /**
     * locale attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                isListenEnable = true
            }
        }
    }
}

@Composable
fun ListItemDetailScreen(
    intent: Intent, viewModel: DeleteContentViewModel
) {
    val context = LocalContext.current
    var isDeleteVisible by remember { mutableStateOf(true) }
    //get extras from intent
    val intentTitle = intent.getStringExtra("title")
    val intentContent = intent.getStringExtra("content")
    val intentFileUrl = intent.getStringExtra("file_url")
    val intentFileType = intent.getStringExtra("fileType")
    val intentId = intent.getStringExtra("_id")
    val intentAuthor = intent.getStringExtra("author")
    if (intentAuthor != null) {
        isDeleteVisible =
            intentAuthor == PreferenceHelper.getInstance(context).getLoggedInUsernameUser()
    } else {
        Toast.makeText(context, "Author not found", Toast.LENGTH_SHORT).show()
    }
    if (isDeleteVisible) {
        Button(onClick = {
            if (intentId != null) {
                viewModel.deleteContent(intentId)
            } else {
                Toast.makeText(context, "Id not found", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(
                text = stringResource(id = R.string.text_delete), textAlign = TextAlign.End
            )
        }
    }
    //title
    if (!intentTitle.isNullOrEmpty()) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(id = R.string.text_title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f)
            )
            Button(onClick = {
                if (isListenEnable) {
                    tts!!.speak(intentTitle, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }) {
                Text(
                    text = stringResource(id = R.string.text_listen), textAlign = TextAlign.End
                )
            }
        }
        Text(text = intentTitle, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp))
    }
    //content
    if (!intentContent.isNullOrEmpty()) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(
                text = stringResource(id = R.string.text_content),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f)
            )
            Button(onClick = {
                if (isListenEnable) {
                    tts!!.speak(intentContent, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }) {
                Text(
                    text = stringResource(id = R.string.text_listen), textAlign = TextAlign.End
                )
            }
        }
        Text(text = intentContent, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp))
    }
    if (!intentContent.isNullOrEmpty()) {
        Text(text = intentContent, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp))
    }
    if (!intentFileUrl.isNullOrEmpty()) {
        when (intentFileType) {
            "audio" -> ShowAudioPlayer(intentFileUrl)
            "image" -> Image(
                modifier = Modifier.padding(16.dp, 8.dp),
                painter = rememberAsyncImagePainter(intentFileUrl),
                contentDescription = null
            )
            "video" -> ShowVideoPlayer(videoUri = Uri.parse(intentFileUrl))
        }
    }
}
