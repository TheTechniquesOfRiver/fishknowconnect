package com.example.fishknowconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.fishknowconnect.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }
}