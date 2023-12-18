package com.example.fishknowconnect.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.ActivityLoginBinding
import com.example.fishknowconnect.ui.MainActivity
import com.example.fishknowconnect.ui.register.RegisterActivity

/**
 * login features
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginViewModelFactory: LoginViewModelFactory
     lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        //viewmodel
        loginViewModelFactory = LoginViewModelFactory()
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java);
        preferenceHelper = PreferenceHelper.getInstance(applicationContext)
        //intialization
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister
        val edittextUsername = binding.editTextTextUsername
        val edittextPassword = binding.editTextTextPassword

        //onclick for register
        buttonRegister.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        })
        //onclick for login
        buttonLogin.setOnClickListener(View.OnClickListener {
            performLogin(edittextUsername, edittextPassword)
        })
    }

    private fun performLogin(edittextUsername: EditText, edittextPassword: EditText) {
        val username =  edittextUsername.text.trim().toString();
        if (username.isEmpty()) {
            edittextUsername.error = resources.getString(R.string.text_username_required)
        } else if (edittextPassword.text.toString().isEmpty()) {
            edittextPassword.error = resources.getString(R.string.text_password_required)
        } else {
            val postData =
                LoginData(username, edittextPassword.text.toString())
            loginViewModel.getLogin(postData)
            loginViewModel.onLoginSuccess.observe(this) { onSuccessLogin ->
                if (onSuccessLogin) {
                    preferenceHelper.setUserLoggedInStatus(true)
                    preferenceHelper.setLoggedInUserUsername(username)
                    Toast.makeText(this, resources.getString(R.string.text_login_successful), Toast.LENGTH_SHORT).show()
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                }
            }
            loginViewModel.onLoginFailure.observe(this) { onFailureLoginMessage ->
                Toast.makeText(this, onFailureLoginMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}