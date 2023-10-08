package com.example.fishknowconnect.ui.privatePost

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.privatePost.ui.theme.FishKnowConnectTheme

class PrivatePostActivity : ComponentActivity() {
    private val viewModel: PrivatePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout(getString(R.string.textview_fish))
                        LaunchedEffect(Unit, block = {
                            viewModel.getAllPrivatePostContent()
                        })
                        PrivatePostScreen("Fish", viewModel)
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
}

/**
 * shows error dialog
 */
@Composable
fun ShowErrorMessage() {
    val context = LocalContext.current as? Activity
    Toast.makeText(
        context, stringResource(id = R.string.text_something_went_wrong), Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun PrivatePostScreen(name: String, viewModel: PrivatePostViewModel,  modifier: Modifier = Modifier) {
    val context = LocalContext.current as? Activity
    when (val responseValue = viewModel.state.collectAsState().value) {
        PrivatePostState.Loading -> IndeterminateCircularIndicator()
        is PrivatePostState.Success -> responseValue.response?.let {
            DisplayPrivateList(it, context, viewModel)
        }
        is PrivatePostState.Error -> ShowErrorMessage()
        else -> {
        }
    }
}
