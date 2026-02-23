package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kindnesshub.databinding.ActivityStartScreenBinding


class StartScreen : AppCompatActivity() {
    private val binding:ActivityStartScreenBinding by lazy {
        ActivityStartScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.userLogin.setOnClickListener{
            val intent=Intent(this,LoginPage::class.java)
            startActivity(intent)

        }

        binding.adminLogin.setOnClickListener{
            val intent=Intent(this, AdminLoginPage::class.java)
            startActivity(intent)
        }
    }
}
