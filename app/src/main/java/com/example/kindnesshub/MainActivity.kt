package com.example.kindnesshub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Fix: Get NavController correctly
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val adminNavController = navHostFragment?.findNavController()

        val adminBottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Fix: Only set up if NavController is not null
        adminNavController?.let {
            adminBottomNav.setupWithNavController(it)
        }
    }
}