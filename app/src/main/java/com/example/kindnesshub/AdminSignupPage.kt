package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kindnesshub.databinding.ActivityAdminSignupPageBinding
import com.example.kindnesshub.model.AdminSignupModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AdminSignupPage : AppCompatActivity() {

    //Signup
    private lateinit var authAdmin: FirebaseAuth
    private lateinit var nameAdmin: String
    private lateinit var emailAdmin: String
    private lateinit var passwordAdmin: String
    private lateinit var repPasswordAdmin: String
    private lateinit var orgName: String
    private lateinit var locationAdmin: String
    private lateinit var databaseAdmin: DatabaseReference

    private val binding: ActivityAdminSignupPageBinding by lazy {
        ActivityAdminSignupPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Initialise authentication
        authAdmin = Firebase.auth
        //create database
        databaseAdmin = Firebase.database.reference

        val locationList = resources.getStringArray(R.array.indian_states)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.createAdminBtn.setOnClickListener {
            //getting data from fields
            locationAdmin = binding.listLocation.text.toString().trim()
            nameAdmin = binding.nameOwner.text.toString().trim()
            orgName = binding.nameOrganisation.text.toString().trim()
            emailAdmin = binding.adminEmail.text.toString().trim()
            passwordAdmin = binding.adminPassword.text.toString().trim()
            repPasswordAdmin = binding.adminRepPassword.text.toString().trim()

            //checking blank fields
            when {
                locationAdmin.isBlank() || nameAdmin.isBlank() || orgName.isBlank() || emailAdmin.isBlank() || passwordAdmin.isBlank() || repPasswordAdmin.isBlank() -> {
                    Toast.makeText(this, "Error: All fields must be filled.", Toast.LENGTH_SHORT).show()
                }
                passwordAdmin != repPasswordAdmin -> {
                    Toast.makeText(this, "Error: Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    createAdminAccount(emailAdmin, passwordAdmin)
                }
            }
        }

        binding.textView7.setOnClickListener{
            val intent = Intent(this, AdminLoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun createAdminAccount(emailAdmin: String, passwordAdmin: String) {
        authAdmin.createUserWithEmailAndPassword(emailAdmin, passwordAdmin).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Success: Registration completed.", Toast.LENGTH_SHORT).show()
                saveAdminData()
                val intent = Intent(this, AdminLoginPage::class.java)
                startActivity(intent)
                finish()
            } else {
                val errorMessage = task.exception?.message ?: "Unknown error occurred."
                Toast.makeText(this, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
                Log.d("Account", "createAdminAccount: Failure", task.exception)
            }
        }
    }

    private fun saveAdminData() {
        //saving database
        locationAdmin=binding.listLocation.text.toString().trim()
        nameAdmin = binding.nameOwner.text.toString().trim()
        orgName = binding.nameOrganisation.text.toString().trim()
        emailAdmin = binding.adminEmail.text.toString().trim()
        passwordAdmin = binding.adminPassword.text.toString().trim()

        val adminUser=AdminSignupModel(locationAdmin,nameAdmin,orgName,emailAdmin,passwordAdmin)
        val adminId:String=FirebaseAuth.getInstance().currentUser!!.uid

        databaseAdmin.child("admin").child(adminId).setValue(adminUser)
    }
}
