package com.example.kindnesshub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kindnesshub.databinding.ActivitySignupPageBinding
import com.example.kindnesshub.model.UserSignupModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignupPage : AppCompatActivity() {

    private lateinit var locationUser:String
    private lateinit var nameUser:String
    private lateinit var emailUser:String
    private lateinit var passwordUser:String
    private lateinit var authUser:FirebaseAuth
    private lateinit var repPasswordUser:String
    private lateinit var databaseUser:DatabaseReference

    private val binding:ActivitySignupPageBinding by lazy {
        ActivitySignupPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        authUser= Firebase.auth
        databaseUser=Firebase.database.reference

        val locationList = resources.getStringArray(R.array.indian_states)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.textView7.setOnClickListener{
            val intent=Intent(this,LoginPage::class.java)
            startActivity(intent)
        }

      binding.createAcBtn.setOnClickListener{

          locationUser=binding.listLocation.text.toString().trim()
          nameUser = binding.userName.text.toString().trim()
          emailUser=binding.userEmail.text.toString().trim()
          passwordUser=binding.userPassword.text.toString().trim()
          repPasswordUser=binding.repUserPassword.text.toString().trim()

          if(locationUser.isBlank() || nameUser.isBlank() || emailUser.isBlank() || passwordUser.isBlank() || repPasswordUser.isBlank()){
              Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
          }else if (passwordUser != repPasswordUser) {
              Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
          }
          else{
              createUserAccount(emailUser,passwordUser)
          }
      }

    }
    private fun createUserAccount(emailUser: String, passwordUser: String) {
        authUser.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent=Intent(this,LoginPage::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this, "Account already created", Toast.LENGTH_SHORT).show()
                Log.d("UserAccount", "createUserAccount: Failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        nameUser = binding.userName.text.toString().trim()
        emailUser=binding.userEmail.text.toString().trim()
        passwordUser=binding.userPassword.text.toString().trim()
        repPasswordUser=binding.repUserPassword.text.toString().trim()

        val user=UserSignupModel(nameUser,emailUser,passwordUser,locationUser)
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
        databaseUser.child("user").child(userId).setValue(user)

    }
}