package com.example.kindnesshub.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object UserFetchDonationHistory {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("donations")

    fun fetchUserDonations(callback: (List<Map<String, String>>, Int) -> Unit) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserEmail == null) {
            Log.e("FetchDonations", "User not logged in")
            callback(emptyList(), 0)
            return
        }

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userDonationsList = mutableListOf<Map<String, String>>()
                var totalAmount = 0

                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.value as? Map<*, *>
                    donation?.let {
                        val userEmail = it["userEmail"] as? String
                        if (userEmail == currentUserEmail) {
                            val amountStr = it["amount"]?.toString() ?: "0"
                            val amount = amountStr.toIntOrNull() ?: 0
                            totalAmount += amount

                            val date = it["date"] as? String ?: "N/A"
                            val orgName = it["orgName"] as? String ?: "N/A"
                            val charityCause = it["charityCause"] as? String ?: "N/A"
                            val location = it["location"] as? String ?: "N/A"

                            userDonationsList.add(
                                mapOf(
                                    "amount" to amountStr,
                                    "date" to date,
                                    "orgName" to orgName,
                                    "charityCause" to charityCause,
                                    "location" to location
                                )
                            )
                        }
                    }
                }

                callback(userDonationsList, totalAmount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchDonations", "Database error: ${error.message}")
                callback(emptyList(), 0)
            }
        })
    }
}

