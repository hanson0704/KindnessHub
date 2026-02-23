package com.example.kindnesshub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kindnesshub.databinding.FragmentBrowseBinding
import com.example.kindnesshub.directory.BrowseFragmentAdapter
import com.example.kindnesshub.repository.AdminDataRepository
import androidx.appcompat.widget.SearchView


class BrowseFragment : Fragment() {

    private lateinit var binding: FragmentBrowseBinding
    private lateinit var adapter: BrowseFragmentAdapter
    private val adminDataRepository = AdminDataRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BrowseFragmentAdapter(mutableListOf(), mutableListOf(), mutableListOf())
        binding.listCharityRV.layoutManager = LinearLayoutManager(requireContext())
        binding.listCharityRV.adapter = adapter

        setupSearchView()  // Initialize search functionality
        fetchDataFromFirebase()
    }


    private fun fetchDataFromFirebase() {
        adminDataRepository.fetchAdminData { charityList ->
            if (charityList.isNotEmpty()) {
                val charityNames = charityList.map { it.orgName }
                val charityOwners = charityList.map { it.nameAdmin }
                val charityLocations = charityList.map { it.location }

                adapter.updateList(charityNames, charityOwners, charityLocations)
            } else {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapter.filter(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "") // Ensure empty string if null
                return true
            }
        })
    }

}
