package com.example.kindnesshub.directory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kindnesshub.databinding.DonatedHistoryBinding

class ContributionFragmentAdapter(
    private var amount: MutableList<String>,
    private var orgName: MutableList<String>,
    private var date: MutableList<String>,
    private var cause: MutableList<String>,
    private var location: MutableList<String> // Added location
) : RecyclerView.Adapter<ContributionFragmentAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(DonatedHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = amount.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(amount[position], orgName[position], date[position], cause[position], location[position])
    }

    fun updateData(newAmounts: List<String>, newOrgNames: List<String>, newDates: List<String>, newCauses: List<String>, newLocations: List<String>) {
        amount.clear()
        amount.addAll(newAmounts)
        orgName.clear()
        orgName.addAll(newOrgNames)
        date.clear()
        date.addAll(newDates)
        cause.clear()
        cause.addAll(newCauses)
        location.clear()
        location.addAll(newLocations) // Update location data

        notifyDataSetChanged() // Refresh RecyclerView
    }

    class HistoryViewHolder(private val binding: DonatedHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(amountDonated: String, organisationName: String, donatedDate: String, donatedCause: String, charityLocation: String) {
            binding.donatedAmounted.text = amountDonated
            binding.donatedDate.text = donatedDate
            binding.orgName.text = organisationName
            binding.donatedCause.text = donatedCause
            binding.orgLocation.text = charityLocation // Display location in UI
        }
    }
}
