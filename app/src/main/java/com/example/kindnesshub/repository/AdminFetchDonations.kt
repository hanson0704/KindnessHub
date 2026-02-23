package com.example.kindnesshub.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object AdminFetchDonations {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("donations")

    // Callback now includes totalAmount as Int
    fun fetchDonationsForAdmin(callback: (List<Map<String, String>>, Int) -> Unit) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserEmail == null) {
            Log.e("FetchDonations", "User not logged in")
            callback(emptyList(), 0)
            return
        }

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val donationsList = mutableListOf<Map<String, String>>()
                var totalAmount = 0

                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.value as? Map<*, *>
                    donation?.let {
                        val emailAdmin = it["emailAdmin"] as? String
                        if (emailAdmin == currentUserEmail) {
                            val amountStr = it["amount"]?.toString() ?: "0"
                            val amount = amountStr.toIntOrNull() ?: 0
                            totalAmount += amount

                            val name = it["userName"] as? String ?: "N/A"
                            val date = it["date"] as? String ?: "N/A"
                            val accountNumber = "XXXX-XXXX" // Masked for security
                            val charityCause = it["charityCause"] as? String ?: "N/A"

                            donationsList.add(
                                mapOf(
                                    "amount" to amountStr,
                                    "name" to name,
                                    "date" to date,
                                    "accountNumber" to accountNumber,
                                    "cause" to charityCause
                                )
                            )
                        }
                    }
                }

                callback(donationsList, totalAmount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchDonations", "Database error: ${error.message}")
                callback(emptyList(), 0)
            }
        })
    }
}
