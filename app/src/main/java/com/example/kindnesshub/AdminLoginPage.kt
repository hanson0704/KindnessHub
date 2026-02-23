package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kindnesshub.databinding.ActivityAdminLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminLoginPage : AppCompatActivity() {

    private lateinit var authAdmin: FirebaseAuth
    private lateinit var databaseAdmin: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityAdminLoginPageBinding by lazy {
        ActivityAdminLoginPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authAdmin = FirebaseAuth.getInstance()
        databaseAdmin = FirebaseDatabase.getInstance().reference

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginBtn.setOnClickListener {
            val email = binding.loginAdminEmail.text.toString().trim()
            val password = binding.loginAdminPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please enter email and password")
                return@setOnClickListener
            }

            authAdmin.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Login successful")
                        navigateToAdminMain()
                    } else {
                        showToast("Login failed: ${task.exception?.message}")
                    }
                }
        }


        binding.gbtn.setOnClickListener {
            signInWithGoogle()
        }

        binding.textView7.setOnClickListener {
            startActivity(Intent(this, AdminSignupPage::class.java))
        }
    }


    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!, account.displayName ?: "Unknown", account.email ?: "")
            } catch (e: ApiException) {
                showToast("Google Sign-In failed: ${e.message}")
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String, name: String, email: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        authAdmin.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = authAdmin.currentUser?.uid
                if (userId != null) {
                    databaseAdmin.child("admin").child(userId).get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            val userMap = mapOf(
                                "locationAdmin" to "N/A", // Not available from Google Sign-In
                                "nameAdmin" to name,
                                "orgName" to "N/A", // Not available from Google Sign-In
                                "emailAdmin" to email,
                                "passwordAdmin" to "N/A" // Google Sign-In users don't have a stored password
                            )

                            databaseAdmin.child("admin").child(userId).setValue(userMap).addOnSuccessListener {
                                showToast("Admin account created successfully")
                                navigateToAdminMain()
                            }.addOnFailureListener {
                                showToast("Failed to save admin data")
                            }
                        } else {
                            navigateToAdminMain()
                        }
                    }.addOnFailureListener {
                        showToast("Database error: ${it.message}")
                    }
                }
            } else {
                showToast("Authentication failed")
            }
        }
    }


    private fun navigateToAdminMain() {
        startActivity(Intent(this, AdminMainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
