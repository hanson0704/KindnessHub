package com.example.kindnesshub.fragment_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kindnesshub.R
import com.example.kindnesshub.databinding.FragmentAdminDashboardBinding
import com.example.kindnesshub.repository.FetchCurrentAdmin

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding
    private val currentAdminData = FetchCurrentAdmin() // Use the repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchAdminData()

        val imageList= ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.home_banner_1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.home_banner_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.home_banner_3, ScaleTypes.FIT))

        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        //App Features
        val imageList2 = ArrayList<SlideModel>()
        imageList2.add(SlideModel(R.drawable.feature_donate, ScaleTypes.FIT))  // Replace with your image resource
        imageList2.add(SlideModel(R.drawable.feature_charity, ScaleTypes.FIT))  // Replace with your image resource
        imageList2.add(SlideModel(R.drawable.feature_contribution, ScaleTypes.FIT))  // Replace with your image resource
        imageList2.add(SlideModel(R.drawable.feature_profile, ScaleTypes.FIT))  // Replace with your image resource


        val imageSlider2 = binding.imageSlider2  // Referencing the second ImageSlider
        imageSlider2.setImageList(imageList2)
        imageSlider2.setImageList(imageList2, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object: ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                //val itemPosition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(),itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAdminData() {
        currentAdminData.fetchCurrentAdminData { adminData ->
            if (adminData != null) {
                binding.nameCharity.text = adminData.orgName
                Log.d("AdminDashboard", "Fetched Org Name: ${adminData.orgName}")
            } else {
                Log.e("AdminDashboard", "Failed to fetch admin data.")
            }
        }
    }
}
