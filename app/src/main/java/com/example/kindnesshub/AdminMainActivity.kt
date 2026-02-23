package com.example.kindnesshub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)
        val adminNavController = navHostFragment?.findNavController()
        val adminBottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)

        adminNavController?.let {
            adminBottomNav.setupWithNavController(it)
        }
    }
}
