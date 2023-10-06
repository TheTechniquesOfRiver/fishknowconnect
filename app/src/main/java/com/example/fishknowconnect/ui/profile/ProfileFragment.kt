package com.example.fishknowconnect.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.databinding.FragmentProfileBinding
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.register.showDialog

class ProfileFragment : Fragment() {
    lateinit var profileViewModelFactory: ProfileViewModelFactory
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        profileViewModelFactory = ProfileViewModelFactory(
            PreferenceHelper.getInstance(requireContext()), FishKnowConnectApi.retrofitService
        )
        val profileViewModel: ProfileViewModel by viewModels(factoryProducer = { profileViewModelFactory })
        binding.composeViewProfile.apply {
            setContent {
                LaunchedEffect(Unit, block = {
                    profileViewModel.getProfileInfo()
                })
                Column {
                    Text(text = profileViewModel.username)
                    ProfileScreen(profileViewModel)
                }
            }
        }
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    when (val response = profileViewModel.state.collectAsState().value) {
        ProfileState.Loading -> IndeterminateCircularIndicator()
        is ProfileState.Success -> SetProfileInfo(response)
        is ProfileState.Error -> showDialog(response.message)
        else -> {
        }
    }
}

/**
 * Set profile information
 */
@Composable
fun SetProfileInfo(response: ProfileState.Success) {
    Column {
        Text(text = "Age")
        Text(text = response.age)
        Text(text = "Location")
        Text(text = response.location)
    }
}

