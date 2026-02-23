package com.example.kindnesshub.fragment_admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.kindnesshub.R
import com.example.kindnesshub.StartScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdminProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var editName: EditText
    private lateinit var editAddress: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var saveButton: View
    private lateinit var logOutButton:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        editName = view.findViewById(R.id.editAdminName)
        editAddress = view.findViewById(R.id.editAdminAddress)
        editEmail = view.findViewById(R.id.editAdminEmail)
        editPhone = view.findViewById(R.id.editAdminPhone)
        saveButton = view.findViewById(R.id.adminSaveButton)
        logOutButton= view.findViewById(R.id.logoutButton)

        val userId = auth.currentUser?.uid

        if (userId != null) {
            loadUserData(userId)
        }

        saveButton.setOnClickListener {
            saveUserData(userId)
        }

        logOutButton.setOnClickListener{
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
        database.child("admin").child(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    editName.setText(snapshot.child("nameAdmin").getValue(String::class.java) ?: "")
                    editAddress.setText(snapshot.child("locationAdmin").getValue(String::class.java) ?: "")
                    editEmail.setText(snapshot.child("emailAdmin").getValue(String::class.java) ?: "")
                    editPhone.setText(snapshot.child("phoneAdmin").getValue(String::class.java) ?: "")
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
            "nameAdmin" to editName.text.toString(),
            "locationAdmin" to editAddress.text.toString(),
            "emailAdmin" to editEmail.text.toString(),
            "phoneAdmin" to editPhone.text.toString()
        )

        database.child("admin").child(userId).updateChildren(updatedUser)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }
}