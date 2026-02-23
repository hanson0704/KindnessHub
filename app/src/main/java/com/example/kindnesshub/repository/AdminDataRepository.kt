package com.example.kindnesshub.repository

import android.util.Log
import com.google.firebase.database.*

class AdminDataRepository {

    private val databaseAdmin: DatabaseReference = FirebaseDatabase.getInstance().getReference("admin")

    fun fetchAdminData(callback: (List<CharityData>) -> Unit) {
        databaseAdmin.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charityList = mutableListOf<CharityData>()

                for (adminSnapshot in snapshot.children) {
                    val nameOwner = adminSnapshot.child("nameAdmin").getValue(String::class.java)
                    val orgName = adminSnapshot.child("orgName").getValue(String::class.java)
                    val location = adminSnapshot.child("locationAdmin").getValue(String::class.java)
                    val emailAdmin = adminSnapshot.child("emailAdmin").getValue(String::class.java) // Fetch email

                    if (nameOwner != null && orgName != null && location != null && emailAdmin != null) {
                        charityList.add(CharityData(orgName, location, nameOwner, emailAdmin))
                    }
                }

                Log.d("FirebaseDebug", "Fetched charities: $charityList")
                callback(charityList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed to load data: ${error.message}")
                callback(emptyList()) // Return an empty list on error
            }
        })
    }

}

data class CharityData(
    val orgName: String,
    val location: String,
    val nameAdmin: String,
    val emailAdmin: String // Now includes email
)

