package com.example.fishknowconnect.ui.register

import LocaleHelper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.CustomWrapWidthIconButton
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.login.LoginActivity
import com.example.fishknowconnect.ui.register.ui.theme.FishKnowConnectTheme
import pl.droidsonroids.gif.GifImageView


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
                    when (val response = viewModel.state.collectAsState().value) {
                        RegistrationState.Loading -> IndeterminateCircularIndicator()
                        is RegistrationState.Success -> OpenLoginScreen()
                        is RegistrationState.Error -> showDialog(response.message)
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
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AndroidView(
            factory = { context ->
                GifImageView(context).apply {
                    setImageResource(R.drawable.register)
                }
            }, modifier = Modifier
                .width(150.dp)
                .height(200.dp)
        )
        OutlinedTextField(
            value = viewModel.username,
            maxLines = 1,
            onValueChange = { username ->
                viewModel.updateUsername(username)
            },
            label = { Text(text = stringResource(R.string.text_username)) },
        )
        OutlinedTextField(value = viewModel.password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            maxLines = 1,
            onValueChange = { password ->
                viewModel.updatePassword(password)
            },
            label = { Text(text = stringResource(R.string.text_password)) })
        OutlinedTextField(value = viewModel.age,
            maxLines = 1,
            onValueChange = { age ->
                viewModel.updateAge(age)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text(text = stringResource(R.string.text_age)) })
        OutlinedTextField(value = viewModel.location, maxLines = 1, onValueChange = { location ->
            viewModel.updateLocation(location)
        }, label = { Text(text = stringResource(R.string.text_location)) })
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        CustomWrapWidthIconButton(
            label = stringResource(id = R.string.title_activity_register),
            icon = R.drawable.icon_register
        ) {
            //check validation
            if (viewModel.username.isEmpty() || viewModel.password.isEmpty() || viewModel.age.isEmpty() || viewModel.location.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                //perform registration
                viewModel.performRegistration()
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
    }
}

/**
 * shows error dialog
 */
@Composable
fun showDialog(message: String) {
    Log.d("register", message)
    Toast.makeText(LocalContext.current.applicationContext, message, Toast.LENGTH_SHORT).show();
}

/***
 * Opens login screen
 */
@Composable
fun OpenLoginScreen() {
    val activity = (LocalContext.current as? Activity)
    Toast.makeText(
        LocalContext.current.applicationContext,
        stringResource(id = R.string.text_succesful_register),
        Toast.LENGTH_SHORT
    ).show();
    val intent = Intent(activity, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
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