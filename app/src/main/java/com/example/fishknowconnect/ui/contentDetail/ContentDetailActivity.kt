package com.example.fishknowconnect.ui.contentDetail

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.contentDetail.ui.theme.FishKnowConnectTheme
import java.util.Locale

var tts: TextToSpeech? = null
var isListenEnable = false

class ContentDetailActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intentTitle = intent.getStringExtra("title")
        val intentContent = intent.getStringExtra("content")
        val intentFileUrl = intent.getStringExtra("file_url")
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
//                        if (intentTitle != null && intentContent != null && intentFileUrl != null) {
                            ListItemDetailScreen(intentTitle, intentContent, intentFileUrl)
//                        } else {
//                            Toast.makeText(
//                                this@ContentDetailActivity,
//                                resources.getString(R.string.text_something_went_wrong),
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                        }
                    }
                }
            }
        }
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
    title: String?, content: String?, fileUrl: String?, modifier: Modifier = Modifier
) {
    Button(onClick = {
        if (isListenEnable) {
            tts!!.speak(content, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }) {
        Text(text = stringResource(id = R.string.text_listen), modifier = modifier)
    }
    if(!title.isNullOrEmpty()){
        Text(text = title, modifier = modifier)
    }
    if(!content.isNullOrEmpty()){
        Text(text = content, modifier = modifier)
    }
    if(!fileUrl.isNullOrEmpty()){
        Image(
            modifier = Modifier.padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(fileUrl),
            contentDescription = null
        )
    }

//    ShowVideoPlayer(videoUri = Uri.parse(fileUrl))
}
