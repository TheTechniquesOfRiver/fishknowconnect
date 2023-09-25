package com.example.fishknowconnect.ui.listItemDetail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.listItemDetail.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostState
import com.example.fishknowconnect.ui.newPost.ShowVideoPlayer

class ListItemDetailActivity : ComponentActivity() {
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
                    Column {
                        ToolBarLayout(resources.getString(R.string.text_content_details))
                        if (intentTitle != null && intentContent != null && intentFileUrl != null) {
                            ListItemDetailScreen(intentTitle, intentContent, intentFileUrl)
                        } else {
                            Toast.makeText(
                                this@ListItemDetailActivity,
                                resources.getString(R.string.text_something_went_wrong),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItemDetailScreen(
    title: String, content: String, fileUrl: String, modifier: Modifier = Modifier
) {
    Text(
        text = title, modifier = modifier
    )
    Text(
        text = content, modifier = modifier
    )
    Image(
        modifier = Modifier.padding(16.dp, 8.dp),
        painter = rememberAsyncImagePainter(fileUrl),
        contentDescription = null
    )
//    ShowVideoPlayer(videoUri = Uri.parse(fileUrl))
}
