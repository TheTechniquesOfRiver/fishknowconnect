package com.example.fishknowconnect.ui.listItemDetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.listItemDetail.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostState

class ListItemDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intentTitle = intent.getStringExtra("title")
        val intentContent = intent.getStringExtra("content")
//        val intentFileUrl = intent.getStringExtra("file_url")

        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout("Content Details")
                        if (intentTitle != null && intentContent != null) {
                            ListItemDetailScreen(intentTitle, intentContent)
                        }else{
                            Toast.makeText(this@ListItemDetailActivity, "Something went wrong", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }
}
}

@Composable
fun ListItemDetailScreen(title: String, content: String, modifier: Modifier = Modifier) {
    Text(
        text = title, modifier = modifier
    )
    Text(
        text = content, modifier = modifier
    )
}
