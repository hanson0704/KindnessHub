package com.example.kindnesshub.directory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kindnesshub.databinding.ListCharitiesBinding

class BrowseFragmentAdapter(
    private var charityNames: MutableList<String>,
    private var charityOwners: MutableList<String>,
    private var charityLocations: MutableList<String>
) : RecyclerView.Adapter<BrowseFragmentAdapter.ViewHolder>() {

    private val originalList = mutableListOf<Triple<String, String, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListCharitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = charityNames[position]
        val owner = charityOwners[position]
        val location = charityLocations[position]
        holder.bind(name, owner, location)
    }

    override fun getItemCount(): Int = charityNames.size

    fun updateList(newNames: List<String>, newOwners: List<String>, newLocations: List<String>) {
        charityNames.clear()
        charityOwners.clear()
        charityLocations.clear()

        charityNames.addAll(newNames)
        charityOwners.addAll(newOwners)
        charityLocations.addAll(newLocations)

        // Update original list for search
        originalList.clear()
        for (i in newNames.indices) {
            originalList.add(Triple(newNames[i], newOwners[i], newLocations[i]))
        }

        notifyDataSetChanged() // Refresh UI
    }

    fun filter(query: String) {
        val searchText = query.lowercase().trim()

        val filteredList = originalList.filter { (name, owner, location) ->
            name.lowercase().contains(searchText) ||
                    owner.lowercase().contains(searchText) ||
                    location.lowercase().contains(searchText)
        }

        charityNames.clear()
        charityOwners.clear()
        charityLocations.clear()

        for ((name, owner, location) in filteredList) {
            charityNames.add(name)
            charityOwners.add(owner)
            charityLocations.add(location)
        }

        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ListCharitiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, owner: String, location: String) {
            binding.orgName.text = name
            binding.orgOwner.text = owner
            binding.orgLocation.text = location
        }
    }
}
