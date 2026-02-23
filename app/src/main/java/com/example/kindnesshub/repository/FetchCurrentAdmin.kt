package com.example.kindnesshub.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchCurrentAdmin{
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun fetchCurrentAdminData(callback: (CurrentAdminData?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val adminId = currentUser.uid
            Log.d("FirebaseAdmin", "Fetching data for Admin ID: $adminId")

            val adminRef = database.child("admin").child(adminId)
            adminRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("FirebaseAdmin", "Snapshot received: ${snapshot.value}")

                    if (snapshot.exists()) {
                        val adminName = snapshot.child("nameAdmin").getValue(String::class.java) ?: "Unknown"
                        val emailAdmin = snapshot.child("emailAdmin").getValue(String::class.java) ?: "Unknown"
                        val orgName = snapshot.child("orgName").getValue(String::class.java) ?: "Unknown"
                        val location = snapshot.child("locationAdmin").getValue(String::class.java) ?: "Unknown"

                        val adminData = CurrentAdminData(adminName, emailAdmin, orgName, location)
                        callback(adminData)
                    } else {
                        Log.e("FirebaseAdmin", "Admin data not found!")
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseAdmin", "Database error: ${error.message}")
                    callback(null)
                }
            })
        } else {
            Log.e("FirebaseAdmin", "No authenticated admin found!")
            callback(null)
        }
    }

}



data class CurrentAdminData(
    val adminName: String,
    val emailAdmin: String,
    val orgName: String,
    val location: String
)