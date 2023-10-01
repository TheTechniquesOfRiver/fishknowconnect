package com.example.fishknowconnect.ui.register

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.login.LoginActivity
import com.example.fishknowconnect.ui.register.ui.theme.FishKnowConnectTheme


class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: RegisterViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen(viewModel = viewModel)
                    when (viewModel.state.collectAsState().value) {
                        RegistrationState.Loading -> IndeterminateCircularIndicator()
                        is RegistrationState.Success -> OpenLoginScreen()
                        is RegistrationState.Error -> showDialog()
                        else -> {
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


/**
 * Main Screen
 *
 */
@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val scrollState = rememberScrollState()
    //Box(modifier = Modifier.verticalScroll(scrollState))
    //{
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            ,horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center) {
            //Row(verticalAlignment = Alignment.CenterVertically) {
            //Image(
                //painter = rememberAsyncImagePainter(R.drawable.list, imageLoader),
                //contentDescription = null,
                //modifier = Modifier
                //.fillMaxWidth()
                //.padding(0.dp, 60.dp, 0.dp, 0.dp)
            //)
            //}
            //Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.title_activity_register),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                ),
                //modifier = Modifier
                //.fillMaxWidth()
                //.padding(0.dp, 280.dp, 0.dp, 0.dp)
            )
            //}
            //}

            //Column(
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally,
            //modifier = Modifier.fillMaxWidth()
            //.padding(0.dp, 280.dp, 0.dp, 0.dp),
            //) {
            OutlinedTextField(value = viewModel.username,
                onValueChange = { username -> viewModel.updateUsername(username) },
                label = { Text(text = stringResource(R.string.text_username)) })
            OutlinedTextField(value = viewModel.password,
                onValueChange = { password -> viewModel.updatePassword(password) },
                label = { Text(text = stringResource(R.string.text_password)) })
            OutlinedTextField(value = viewModel.age,
                onValueChange = { age -> viewModel.updateAge(age) },
                label = { Text(text = stringResource(R.string.text_age)) })
            OutlinedTextField(value = viewModel.location,
                onValueChange = { location -> viewModel.updateLocation(location) },
                label = { Text(text = stringResource(R.string.text_location)) })
            Button(modifier = Modifier
                .height(81.dp)
                .width(287.dp)
                .padding(0.dp, 10.dp), onClick = {
                //perform registration
                viewModel.performRegistration()
            }) {
                Text(text = stringResource(R.string.title_activity_register),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }

        }
    //}

}

/**
 * shows error dialog
 */
fun showDialog() {
    Log.d("result", "error")
    //TODO show error dialog
}

/***
 * Opens login screen
 */
@Composable
fun OpenLoginScreen() {
    val activity = (LocalContext.current as? Activity)
    val intent = Intent(activity, LoginActivity::class.java)
    activity?.startActivity(intent)
    activity?.finish()
}

/**
 * shows preview
 */
@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    FishKnowConnectTheme {
        Surface {
            val registerViewModel = RegisterViewModel()
            RegisterScreen(registerViewModel)
        }
    }
}

/**
 * shows preview
 */
@Preview(showBackground = true, locale = "bn")
@Composable
fun RegisterBangalaPreview() {
    FishKnowConnectTheme {
        Surface {
            val registerViewModel = RegisterViewModel()
            RegisterScreen(registerViewModel)
        }
    }
}