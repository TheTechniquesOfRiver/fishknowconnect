package com.example.fishknowconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.selectLanguage.SelectLanguage

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@SplashScreenActivity, SelectLanguage::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }
}