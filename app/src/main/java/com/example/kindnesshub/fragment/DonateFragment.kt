package com.example.kindnesshub.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kindnesshub.R
import com.example.kindnesshub.databinding.FragmentDonateBinding
import com.example.kindnesshub.repository.AdminDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class DonateFragment : Fragment() {
    private lateinit var binding: FragmentDonateBinding
    private val adminRepository = AdminDataRepository()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val charityEmailMap = mutableMapOf<String, String>()
    private val charityLocationMap = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        populateCharityNames()
        setupDonationCauseDropdown()
        setupPaymentMethodDropdown()


        binding.donateButton.setOnClickListener { validateAndDonate() }
    }


    private fun setupPaymentMethodDropdown(){
        val paymentMethod = resources.getStringArray(R.array.payment_method)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, paymentMethod)
        binding.listPaytMethod.setAdapter(adapter)
    }

    private fun populateCharityNames() {
        adminRepository.fetchAdminData { adminList ->
            if (adminList.isNotEmpty()) {
                val charityNames = adminList.map { admin ->
                    charityEmailMap[admin.orgName] = admin.emailAdmin
                    charityLocationMap[admin.orgName] = admin.location
                    admin.orgName
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, charityNames)
                binding.listCharity.setAdapter(adapter)
            } else {
                Log.e("FirebaseError", "No admin data found")
            }
        }
    }

    private fun setupDonationCauseDropdown() {
        val donationCause = resources.getStringArray(R.array.donation_cause)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, donationCause)
        binding.listCause.setAdapter(adapter)
    }

    private fun validateAndDonate() {
        val charity = binding.listCharity.text.toString().trim()
        val cause = binding.listCause.text.toString().trim()
        val amountStr = binding.amount.text.toString().trim()
        val payt=binding.listPaytMethod.text.toString().trim()

        when {
            charity.isEmpty() -> showToast("Please select a charity")
            cause.isEmpty() -> showToast("Please select a cause")
            payt.isEmpty()-> showToast("Please select the Payment Method")
            amountStr.isEmpty() || amountStr.toDoubleOrNull() == null || amountStr.toDouble() <= 0 ->
                showToast("Enter a valid donation amount")
            else -> {
                val adminEmail = charityEmailMap[charity] ?: "Unknown"
                val location = charityLocationMap[charity] ?: "Unknown"
                val amount = amountStr.toDouble()

                saveDonation(charity, cause, amount, adminEmail, location)
            }
        }
    }

    private fun saveDonation(orgName: String, cause: String, amount: Double, adminEmail: String, location: String) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid == null) {
            showToast("User not logged in")
            return
        }

        database.child("user").child(uid).child("name").get().addOnSuccessListener { snapshot ->
            val userName = snapshot.getValue(String::class.java) ?: "Unknown"
            val userEmail = user.email ?: "No email"

            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            val donation = Donation(userName, userEmail, orgName, cause, amount, date, time, adminEmail, location)
            val donationId = database.child("donations").push().key

            if (donationId != null) {
                database.child("donations").child(donationId).setValue(donation)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast("Donation successful!")
                        } else {
                            showToast("Donation failed! Please try again.")
                        }
                    }
            }
        }.addOnFailureListener {
            showToast("Failed to retrieve user data")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

// Donation data class
data class Donation(
    val userName: String,
    val userEmail: String,
    val orgName: String,
    val charityCause: String,
    val amount: Double,
    val date: String,
    val time: String,
    val emailAdmin: String,
    val location: String
)
