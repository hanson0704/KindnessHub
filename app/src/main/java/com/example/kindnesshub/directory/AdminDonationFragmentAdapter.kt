package com.example.kindnesshub.directory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kindnesshub.databinding.DonationDetailsBinding

class AdminDonationFragmentAdapter(
    private val amount: List<String>,
    private val donorName: List<String>,
    private val accNo: List<String>,
    private val date: List<String>,
    private val cause: List<String>
) : RecyclerView.Adapter<AdminDonationFragmentAdapter.DonationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        return DonationViewHolder(DonationDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val amountReceived = amount[position]
        val name = donorName[position]
        val accountNumber = accNo[position]
        val dateDonated = date[position]
        val donationCause = cause[position]

        holder.bind(amountReceived, name, accountNumber, dateDonated, donationCause)
    }

    override fun getItemCount(): Int {
        return amount.size
    }

    class DonationViewHolder(private val binding: DonationDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receivedAmount: String, name: String, accountNumber: String, dateDonated: String, donationCause: String) {
            binding.donorName.text = name
            binding.donationDate.text = dateDonated
            binding.receivedAmounted.text = receivedAmount
            binding.accNo.text = accountNumber
            binding.donationCause.text = donationCause  // ✅ Ensure this field is in `DonationDetailsBinding`
        }
    }
}
