package com.example.fishknowconnect.ui.weather

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.DisplayList
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.CustomFullWidthIconButton
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.showError
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.TypeState
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.NewPostActivity
import com.example.fishknowconnect.ui.privatePost.PrivatePostActivity

class WeatherActivity() : ComponentActivity() {
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var preferenceHelper: PreferenceHelper
    val viewModel: WeatherViewModel by viewModels(factoryProducer = { weatherViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper.getInstance(this)
        weatherViewModelFactory = WeatherViewModelFactory(
            preferenceHelper, FishKnowConnectApi.retrofitService
        )
        setContent {
            FishKnowConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ToolBarLayout(this@WeatherActivity.getString(R.string.textview_weather))
                        WeatherScreen()
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            TypeState.Loading -> IndeterminateCircularIndicator()
                            is TypeState.Success -> responseValue.response?.let {
                                DisplayList(it, stringResource(id = R.string.text_latest_post))
                            }

                            is TypeState.Error -> showError()
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
        viewModel.getAllWeatherContents()
    }
}


/**
 * Weather screen view
 */
@Composable
fun WeatherScreen() {
    val activity = (LocalContext.current as? Activity)
    Column(modifier = Modifier.padding(10.dp)) {
        CustomFullWidthIconButton(
            label = stringResource(id = R.string.button_new_post), icon = R.drawable.plus
        ) {
            //start new post screen
            val intent = Intent(activity, NewPostActivity::class.java).apply {
                putExtra("type", "Weather")
            }
            activity?.startActivity(intent)
        }
        CustomFullWidthIconButton(
            label = stringResource(id = R.string.button_private_post), icon = R.drawable.view
        ) {
            //start private post screen
            val intent = Intent(activity, PrivatePostActivity::class.java).apply {
                putExtra("type", "Weather")
            }
            activity?.startActivity(intent)
        }
    }
}
