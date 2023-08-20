package com.example.fishknowconnect.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.fishknowconnect.NavigationDrawerActivity
import com.example.fishknowconnect.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, NavigationDrawerActivity::class.java)
            startActivity(i)
            finish()
        })
    }
}