package com.example.kindnesshub.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kindnesshub.databinding.FragmentContributionBinding
import com.example.kindnesshub.directory.ContributionFragmentAdapter
import com.example.kindnesshub.repository.UserFetchDonationHistory

class ContributionFragment : Fragment() {

    private lateinit var binding: FragmentContributionBinding
    private lateinit var donationAdapter: ContributionFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContributionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donationAdapter = ContributionFragmentAdapter(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
        binding.contributions.layoutManager = LinearLayoutManager(requireContext())
        binding.contributions.adapter = donationAdapter

        // Fetch user-specific donation history and total amount
        UserFetchDonationHistory.fetchUserDonations { userDonations, totalAmount ->
            updateRecyclerView(userDonations)
            binding.totalDonationUser.text = totalAmount.toString()
        }
    }


    private fun updateRecyclerView(userDonations: List<Map<String, String>>) {
        val amounts = userDonations.map { it["amount"] ?: "N/A" }.toMutableList()
        val orgNames = userDonations.map { it["orgName"] ?: "N/A" }.toMutableList()
        val dates = userDonations.map { it["date"] ?: "N/A" }.toMutableList()
        val causes = userDonations.map { it["charityCause"] ?: "N/A" }.toMutableList()
        val locations = userDonations.map { it["location"] ?: "N/A" }.toMutableList() // Fetching location

        donationAdapter.updateData(amounts, orgNames, dates, causes, locations)
    }
}
