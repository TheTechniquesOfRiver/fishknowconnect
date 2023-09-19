package com.example.fishknowconnect.ui.fish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.ListContent
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ToolBarLayout
import com.example.fishknowconnect.ui.fish.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostActivity

class FishActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                FishKnowConnectTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            ToolBarLayout("Fish")
                            FishScreen("Fish")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Fish screen view
 */
@Composable
fun FishScreen(name: String, modifier: Modifier = Modifier) {
    val activity = (LocalContext.current as? Activity)
    //title
    Text(
        text = "Latest $name posts", modifier = modifier
    )
    //create new post button
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
        horizontalAlignment = Alignment.End,
    ) {
        Button(onClick = {
            //start new post screen
            val intent = Intent(activity, NewPostActivity::class.java)
            activity?.startActivity(intent)
        }) {
            Text(
                text = stringResource(id = R.string.button_new_post),
                textAlign = TextAlign.Right,
            )
        }
    }
    //show list
    ListContent()
}

@Preview(showBackground = true)
@Composable
fun FishScreenPreview() {
    FishKnowConnectTheme {
        FishScreen("Fishscreen")
    }
}