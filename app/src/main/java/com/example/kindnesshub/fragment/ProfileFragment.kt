package com.example.kindnesshub.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kindnesshub.R
import com.example.kindnesshub.StartScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var editName: EditText
    private lateinit var editAddress: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var saveButton: View
    private lateinit var logOutButton: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        editName = view.findViewById(R.id.editName)
        editAddress = view.findViewById(R.id.editAddress)
        editEmail = view.findViewById(R.id.editEmail)
        editPhone = view.findViewById(R.id.editPhone)
        saveButton = view.findViewById(R.id.saveButton)
        logOutButton = view.findViewById(R.id.logoutButton)

        val userId = auth.currentUser?.uid

        if (userId != null) {
            loadUserData(userId)
        }

        saveButton.setOnClickListener {
            saveUserData(userId)
        }

        logOutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(),"Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), StartScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    private fun loadUserData(userId: String) {
        database.child("user").child(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    editName.setText(snapshot.child("name").getValue(String::class.java) ?: "")
                    editAddress.setText(snapshot.child("location").getValue(String::class.java) ?: "")
                    editEmail.setText(snapshot.child("email").getValue(String::class.java) ?: "")
                    editPhone.setText(snapshot.child("phone").getValue(String::class.java) ?: "")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData(userId: String?) {
        if (userId == null) return

        val updatedUser = mapOf(
            "name" to editName.text.toString(),
            "location" to editAddress.text.toString(),
            "email" to editEmail.text.toString(),
            "phone" to editPhone.text.toString()
        )

        database.child("user").child(userId).updateChildren(updatedUser)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }
}
