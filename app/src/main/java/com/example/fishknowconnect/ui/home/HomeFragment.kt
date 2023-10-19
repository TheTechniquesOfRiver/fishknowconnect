package com.example.fishknowconnect.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.FragmentHomeBinding
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.MainActivity
import com.example.fishknowconnect.ui.login.LoginViewModel
import com.example.fishknowconnect.ui.login.LoginViewModelFactory


class HomeFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private var navController: NavController? = null
    lateinit var preferenceHelper: PreferenceHelper
    lateinit var homeApprovalCountViewModel: HomeApprovalCountViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var textViewCount: TextView
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
        //viewmodel
        homeViewModelFactory = HomeViewModelFactory(
            preferenceHelper, retrofitService = FishKnowConnectApi.retrofitService
        )
        homeApprovalCountViewModel = ViewModelProvider(
            this, homeViewModelFactory
        ).get(HomeApprovalCountViewModel::class.java);
        view.findViewById<TextView>(R.id.textview_welcome_home).text =
            getString(R.string.text_welcome) + " " + preferenceHelper.getLoggedInUsernameUser() + " !"
        view.findViewById<ImageView>(R.id.imageViewFish).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWeather).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewBoat).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWater).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewOthers).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewApprovePost).setOnClickListener(this)
        view.findViewById<TextView>(R.id.textViewCreateNewPostHome).setOnClickListener(this)
        textViewCount = view.findViewById<TextView>(R.id.textViewCount)


    }

    override fun onResume() {
        super.onResume()
        //get count
        homeApprovalCountViewModel.getApprovalCount()
        homeApprovalCountViewModel.onSuccess.observe(this) { onSuccess ->
            if(onSuccess == "0"){
                textViewCount.visibility = View.GONE
            }else{
                textViewCount.setText(onSuccess)
            }
        }
        homeApprovalCountViewModel.onFailure.observe(this) { onFailure ->
            textViewCount.setText(onFailure)
        }
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