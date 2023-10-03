package com.example.fishknowconnect.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.ActivityLoginBinding
import com.example.fishknowconnect.ui.NavigationDrawerActivity
import com.example.fishknowconnect.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginViewModelFactory: LoginViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceHelper.setUserLoggedInStatus(this, false)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        //viewmodel
        loginViewModelFactory = LoginViewModelFactory()
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java);
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //intialization
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val edittextUsername = findViewById<EditText>(R.id.editTextTextUsername)
        val edittextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        //onclick for register
        buttonRegister.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        })
        //onclick for login
        buttonLogin.setOnClickListener(View.OnClickListener {
            if (edittextUsername.text.toString().isEmpty()) {
                edittextUsername.error = resources.getString(R.string.text_username_required)
            } else if (edittextPassword.text.toString().isEmpty()) {
                edittextPassword.error = resources.getString(R.string.text_password_required)
            } else {
                val postData =
                    LoginData(edittextUsername.text.toString(), edittextPassword.text.toString())
                loginViewModel.getLogin(postData)
                loginViewModel.onLoginSuccess.observe(this) { onSuccessLogin ->
                    if (onSuccessLogin) {
                        PreferenceHelper.setUserLoggedInStatus(this, true)
                        val i = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                        finish()
                    }
                }
                loginViewModel.onLoginFailure.observe(this) { onFailureLoginMessage ->
                    Toast.makeText(this, onFailureLoginMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}