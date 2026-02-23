package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WelcomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null) {
                checkUserType(currentUser.uid)
            } else {
                startActivity(Intent(this, StartScreen::class.java))
                finish()
            }
        }, 3000)
    }


    //check if account exists
    private fun checkUserType(userId: String) {
        database.child("user").child(userId).get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                database.child("admin").child(userId).get().addOnSuccessListener { adminSnapshot ->
                    if (adminSnapshot.exists()) {
                        startActivity(Intent(this, AdminMainActivity::class.java))
                    } else {
                        startActivity(Intent(this, StartScreen::class.java))
                    }
                }
            }
            finish()
        }
    }
}
