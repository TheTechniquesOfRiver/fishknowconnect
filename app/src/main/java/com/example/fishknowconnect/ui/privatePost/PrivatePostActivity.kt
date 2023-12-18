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
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.showError
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.privatePost.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.showErrorMessage
/**
 * gets private post information
 */
class PrivatePostActivity : ComponentActivity() {
    lateinit var privatePostViewModelFactory: PrivatePostViewModelFactory
    lateinit var preferenceHelper: PreferenceHelper
    val viewModel: PrivatePostViewModel by viewModels(factoryProducer = { privatePostViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper.getInstance(this)
        privatePostViewModelFactory = PrivatePostViewModelFactory(
            preferenceHelper, FishKnowConnectApi.retrofitService
        )
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val intentType = intent.getStringExtra("type")
                        val typeTitle = intent.getStringExtra("typeTitle")
                        if (intentType != null) {
                            if (typeTitle != null) {
                                ToolBarLayout(typeTitle)
                            }
                            LaunchedEffect(Unit, block = {
                                viewModel.getAllPrivatePostContent(intentType)
                            })
                            PrivatePostScreen(intentType, viewModel)
                        } else {
                            ToolBarLayout("")
                            Toast.makeText(
                                this@PrivatePostActivity, "type not found", Toast.LENGTH_SHORT
                            ).show()
                            finish()
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
}

@Composable
fun PrivatePostScreen(
    name: String, viewModel: PrivatePostViewModel, modifier: Modifier = Modifier
) {
    val context = LocalContext.current as? Activity
    //for displaying list
    when (val responseValue = viewModel.state.collectAsState().value) {
        PrivatePostState.Loading -> IndeterminateCircularIndicator()
        is PrivatePostState.Success -> responseValue.response?.let {
            DisplayPrivateList(it, context, viewModel)
        }

        is PrivatePostState.Error -> showErrorMessage(stringResource(id = R.string.text_no_post))
        else -> {
        }
    }
//for sending request
    when (val responseValue = viewModel.state.collectAsState().value) {
        PrivatePostState.Loading -> IndeterminateCircularIndicator()
        is PrivatePostState.SuccessAccess -> responseValue.message?.let {
//        Toast.makeText(
//                    context, responseValue.message, Toast.LENGTH_SHORT
//            ).show()
            Toast.makeText(
                context,
                stringResource(id = R.string.text_send_request_for_access),
                Toast.LENGTH_SHORT
            ).show()
            context?.finish()
        }

        is PrivatePostState.Error -> showErrorMessage(stringResource(id = R.string.text_no_post))
        is PrivatePostState.Failure -> showErrorMessage(stringResource(id = R.string.text_no_post))
        else -> {
        }
    }
}
