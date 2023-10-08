package com.example.fishknowconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view);
        view.findViewById<ImageView>(R.id.imageViewFish).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWeather).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewBoat).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewWater).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageViewOthers).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageButton).setOnClickListener(this)
    }

    /**
     * upon imageview click
     */
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imageViewFish -> {
                    navController?.navigate(R.id.action_nav_home_to_fishActivity)
                }
                R.id.imageViewWeather -> {
                    navController?.navigate(R.id.action_nav_home_to_weatherActivity)
                }
                R.id.imageViewWater -> {
                    navController?.navigate(R.id.action_nav_home_to_waterActivity)
                }
                R.id.imageViewBoat -> {
                    navController?.navigate(R.id.action_nav_home_to_boatActivity)
                }
                R.id.imageViewOthers -> {
                    navController?.navigate(R.id.action_nav_home_to_othersActivity)
                }
                R.id.imageButton -> {
                    navController?.navigate(R.id.action_nav_home_to_approvePostRequestFragment)
                }
            }
        }
    }
}