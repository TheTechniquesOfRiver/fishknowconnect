package com.example.fishknowconnect.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.fishknowconnect.NavigationDrawerActivity
import com.example.fishknowconnect.databinding.ActivityLoginBinding
import java.util.Locale


class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginViewModelFactory: LoginViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Locale", ""+ Locale.getDefault())

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        //viewmodel
        loginViewModelFactory = LoginViewModelFactory()
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java);
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //intialization
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val edittextUsername = findViewById<EditText>(R.id.editTextTextUsername)
        val edittextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        buttonLogin.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
            startActivity(i)
//            val postData = LoginData(edittextUsername.text.toString(), edittextPassword.text.toString())
//            loginViewModel.getLogin(postData)
//            loginViewModel.onLoginSuccess.observe(this) { onSuccessLogin ->
//                if (onSuccessLogin) {
//                    val i = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
//                    startActivity(i)
//                    finish()
//                }
//            }
        })
    }
}