package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class AdminScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_screen)

        // Hide Action Bar
        supportActionBar?.hide()

        // Delay transition to AdminLoginPage
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AdminLoginPage::class.java))
            finish()
        }, 3000)
    }
}
