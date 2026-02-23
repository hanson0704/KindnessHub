package com.example.kindnesshub.fragment_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kindnesshub.databinding.FragmentAdminDonationBinding
import com.example.kindnesshub.directory.AdminDonationFragmentAdapter
import com.example.kindnesshub.repository.AdminFetchDonations

class AdminDonationFragment : Fragment() {

    private lateinit var binding: FragmentAdminDonationBinding
    private lateinit var donationAdapter: AdminDonationFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donationAdapter = AdminDonationFragmentAdapter(
            mutableListOf(), mutableListOf(),
            mutableListOf(), mutableListOf(), mutableListOf()
        )
        binding.Donations.layoutManager = LinearLayoutManager(requireContext())
        binding.Donations.adapter = donationAdapter

        // Updated to handle both donations list and total amount
        AdminFetchDonations.fetchDonationsForAdmin { donationsList, totalAmount ->
            updateRecyclerView(donationsList)
            binding.totalDonation.text = totalAmount.toString()
        }
    }

    private fun updateRecyclerView(donationsList: List<Map<String, String>>) {
        val amounts = donationsList.map { it["amount"] ?: " " }
        val names = donationsList.map { it["name"] ?: "N/A" }
        val dates = donationsList.map { it["date"] ?: "N/A" }
        val accountNumbers = donationsList.map { it["accountNumber"] ?: "XXXX-XXXX" }
        val charityCauses = donationsList.map { it["cause"] ?: "N/A" }

        donationAdapter = AdminDonationFragmentAdapter(amounts, names, accountNumbers, dates, charityCauses)
        binding.Donations.adapter = donationAdapter
    }
}
