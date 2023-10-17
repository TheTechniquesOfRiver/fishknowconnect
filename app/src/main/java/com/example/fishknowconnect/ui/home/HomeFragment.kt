package com.example.fishknowconnect.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private var navController: NavController? = null
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view);
        preferenceHelper = PreferenceHelper.getInstance(requireContext())
        view.findViewById<TextView>(R.id.textview_welcome_home).text =
            getString(R.string.text_welcome) +" "+  preferenceHelper.getLoggedInUsernameUser() + " !"
        view.findViewById<ImageView>(R.id.imageViewFish).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWeather).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewBoat).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWater).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewOthers).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewApprovePost).setOnClickListener(this)
        view.findViewById<TextView>(R.id.textViewCreateNewPostHome).setOnClickListener(this)
    }

    /**
     * upon imageview click
     */
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imageViewFish -> {
                    navController?.navigate(R.id.action_navigation_home_to_fishActivity)
                }
                R.id.imageViewWeather -> {
                    navController?.navigate(R.id.action_navigation_home_to_weatherActivity)
                }
                R.id.imageViewWater -> {
                    navController?.navigate(R.id.action_navigation_home_to_waterActivity)
                }
                R.id.imageViewBoat -> {
                    navController?.navigate(R.id.action_navigation_home_to_boatActivity)
                }
                R.id.imageViewOthers -> {
                    navController?.navigate(R.id.action_navigation_home_to_othersActivity)
                }
                R.id.imageViewApprovePost -> {
                    navController?.navigate(R.id.action_navigation_home_to_approvePostRequestActivity)
                }
                R.id.textViewCreateNewPostHome -> {
                    navController?.navigate(R.id.action_navigation_home_to_homeNewPostActivity)
                }
            }
        }
    }
}