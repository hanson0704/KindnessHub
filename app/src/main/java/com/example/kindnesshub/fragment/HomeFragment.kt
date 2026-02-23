package com.example.kindnesshub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kindnesshub.R

import com.example.kindnesshub.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

//    private lateinit var binding:FragmentName
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding= FragmentName.inflate(inflater,container,false )
//        return binding.root
//    }

    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //BANNER
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

        imageSlider.setItemClickListener(object: ItemClickListener{
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

}