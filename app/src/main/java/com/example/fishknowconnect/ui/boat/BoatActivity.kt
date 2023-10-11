package com.example.fishknowconnect.ui.boat

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.DisplayList
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ShowErrorMessage
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.TypeState
import com.example.fishknowconnect.ui.boat.BoatViewModel
import com.example.fishknowconnect.ui.fish.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostActivity
import com.example.fishknowconnect.ui.privatePost.PrivatePostActivity

class BoatActivity : ComponentActivity() {

    val viewModel: BoatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout(this@BoatActivity.getString(R.string.textview_boat))
                        BoatScreen(this@BoatActivity.getString(R.string.textview_boat), viewModel)
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            TypeState.Loading -> IndeterminateCircularIndicator()
                            is TypeState.Success -> responseValue.response?.let {
                                DisplayList(it)
                            }
                            is TypeState.Error -> ShowErrorMessage()
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * locale attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    override fun onResume() {
        super.onResume()
        viewModel.getAllBoatContents()
    }
}


/**
 * Weather screen view
 */
@Composable
fun BoatScreen(name: String, viewModel: BoatViewModel) {
    val activity = (LocalContext.current as? Activity)
    Row() {
        //private post button
        Button(modifier = Modifier.padding(vertical = 25.dp), onClick = {
            //start new post screen
            val intent = Intent(activity, PrivatePostActivity::class.java).apply {
                putExtra("type", "Boat")
            }
            activity?.startActivity(intent)
        }) {
            Text(
                text = stringResource(id = R.string.button_private_post),
                textAlign = TextAlign.Left,
                style = TextStyle(
                    fontSize = 20.sp, color = Color.White, fontFamily = FontFamily.SansSerif
                )
            )
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp)
            )

        }
        //create new post button
        Button(modifier = Modifier.padding(vertical = 25.dp), onClick = {
            //start new post screen
            val intent = Intent(activity, NewPostActivity::class.java).apply {
                putExtra("type", "Boat")
            }
            activity?.startActivity(intent)
        }) {
            Text(
                text = stringResource(id = R.string.button_new_post),
                textAlign = TextAlign.Right,
                style = TextStyle(
                    fontSize = 20.sp, color = Color.White, fontFamily = FontFamily.SansSerif
                )
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp)
            )
        }
    }

    //title
    Text(
        text = stringResource(id = R.string.text_latest_post), style = TextStyle(
            fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun BoatScreenPreview() {
    FishKnowConnectTheme {
        val viewModel = BoatViewModel()
        BoatScreen("BoatScreen", viewModel)
    }
}
