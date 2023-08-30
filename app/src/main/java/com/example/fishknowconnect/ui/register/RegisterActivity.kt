package com.example.fishknowconnect.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
}


/**
 * Main Screen
 *
 */
@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        OutlinedTextField(value = viewModel.username,
            onValueChange = { username -> viewModel.updateUsername(username) },
            label = { Text(text = "username") })
        OutlinedTextField(value = viewModel.password,
            onValueChange = { password -> viewModel.updatePassword(password) },
            label = { Text(text = "password") })
        OutlinedTextField(value = viewModel.phone,
            onValueChange = { phone -> viewModel.updatePhone(phone) },
            label = { Text(text = "phone") })
        OutlinedTextField(value = viewModel.id,
            onValueChange = { id -> viewModel.updateId(id) },
            label = { Text(text = "id") })
        Button(onClick = {
            //perform registration
            viewModel.performRegistration()
        }) {
            Text(text = "Register")
        }

    }
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
    val mContext = LocalContext.current
    mContext.startActivity(Intent(mContext, LoginActivity::class.java))
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