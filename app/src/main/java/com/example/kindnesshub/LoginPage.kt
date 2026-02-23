package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kindnesshub.databinding.ActivityLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginPage : AppCompatActivity() {

    private lateinit var authUser: FirebaseAuth
    private lateinit var databaseUser: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginPageBinding by lazy {
        ActivityLoginPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authUser = FirebaseAuth.getInstance()
        databaseUser = FirebaseDatabase.getInstance().reference

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.gbtn.setOnClickListener {
            signInWithGoogle()
        }

        binding.textView7.setOnClickListener {
            startActivity(Intent(this, SignupPage::class.java))
        }

        binding.loginBtn.setOnClickListener {
            val emailUser = binding.loginUserEmail.text.toString().trim()
            val passwordUser = binding.loginUserPassword.text.toString().trim()

            if (emailUser.isBlank() || passwordUser.isBlank()) {
                showToast("Please fill all the fields")
            } else {
                loginUserAccount(emailUser, passwordUser)
            }
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
        authUser.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = authUser.currentUser?.uid
                if (userId != null) {
                    databaseUser.child("user").child(userId).get().addOnSuccessListener {
                        if (!it.exists()) {
                            val userMap = mapOf("name" to name, "email" to email)
                            databaseUser.child("user").child(userId).setValue(userMap).addOnSuccessListener {
                                showToast("User account created successfully")
                                navigateToMain()
                            }
                        } else {
                            navigateToMain()
                        }
                    }
                }
            } else {
                showToast("Authentication failed")
            }
        }
    }

    private fun loginUserAccount(email: String, password: String) {
        authUser.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUserInDatabase()
            } else {
                showToast("Incorrect Password")
            }
        }
    }

    private fun checkUserInDatabase() {
        val userId = authUser.currentUser?.uid
        if (userId != null) {
            databaseUser.child("user").child(userId).get().addOnSuccessListener {
                if (it.exists()) {
                    showToast("Logged in")
                    navigateToMain()
                } else {
                    showToast("User data not found")
                }
            }
        } else {
            showToast("No user logged in")
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
