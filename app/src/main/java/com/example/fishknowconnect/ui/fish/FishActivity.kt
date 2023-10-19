package com.example.fishknowconnect.ui.fish

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.DisplayList
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.CustomFullWidthIconButton
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.ShowErrorMessage
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.TypeState
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostActivity
import com.example.fishknowconnect.ui.privatePost.PrivatePostActivity

class FishActivity : ComponentActivity() {
    lateinit var fishViewModelFactory: FishViewModelFactory
    lateinit var preferenceHelper: PreferenceHelper
    val viewModel: FishViewModel by viewModels(factoryProducer = { fishViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper.getInstance(this)
        fishViewModelFactory = FishViewModelFactory(
            preferenceHelper, FishKnowConnectApi.retrofitService
        )
        setContent {
            FishKnowConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout(this@FishActivity.getString(R.string.textview_fish))
                        FishScreen()
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            TypeState.Loading -> IndeterminateCircularIndicator()
                            is TypeState.Success -> responseValue.response?.let {
                                DisplayList(it, stringResource(id = R.string.text_latest_post))
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
        viewModel.getAllFishContents()
    }
}


/**
 * Fish screen view
 */
@Composable
fun FishScreen() {
    val activity = (LocalContext.current as? Activity)
    Column(modifier = Modifier.padding(10.dp)) {
        //create new post button
        OutlinedButton(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
            border = BorderStroke(2.dp, Color.Black), onClick = {
                //start new post screen
                val intent = Intent(activity, NewPostActivity::class.java).apply {
                    putExtra("type", "Fish")
                }
                activity?.startActivity(intent)
            }) {
            Text(
                text = stringResource(id = R.string.button_new_post),
                Modifier
                    .padding(start = 10.dp)
                    .height(40.dp),
                style = TextStyle(
                    fontSize = 18.sp, fontFamily = FontFamily.SansSerif, color = Color.Black
                )
            )
            Image(
                modifier = Modifier
                    .width(56.dp)
                    .height(26.dp)
                    .padding(2.dp),
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "createpost")
        }
        CustomFullWidthIconButton(label = "view pirvate post", icon = R.drawable.icon_voice, onClick = {

            //start private post screen
            val intent = Intent(activity, PrivatePostActivity::class.java).apply {
                putExtra("type", "Fish")
            }
            activity?.startActivity(intent)
        })
        //private post button
        Button(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(), onClick = {
            //start private post screen
            val intent = Intent(activity, PrivatePostActivity::class.java).apply {
                putExtra("type", "Fish")
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
                painter = painterResource(R.drawable.view),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp)
            )
        }

    }
}


