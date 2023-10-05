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
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.databinding.FragmentProfileBinding
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.register.OpenLoginScreen
import com.example.fishknowconnect.ui.register.showDialog

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    val profileViewModel: ProfileViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        this.context?.let {
            PreferenceHelper.getLoggedInUsernameUser(it)
                ?.let { profileViewModel._username(it) }
        }


        //viewmodel
        binding.composeViewProfile.apply {
            setContent {

                LaunchedEffect(Unit, block = {
                    profileViewModel.getProfileInfo()
                })

                ProfileScreen(profileViewModel)
                when (val response = profileViewModel.state.collectAsState().value) {
                    ProfileState.Loading -> IndeterminateCircularIndicator()
                    is ProfileState.Success -> SetProfileInfo(response)
                    is ProfileState.Error -> showDialog(response.message)
                    else -> {
                    }
                }

            }
        }
        val root: View = binding.root
        return root
    }

    @Composable
    private fun SetProfileInfo(response: ProfileState.Success) {
        Column {
            val context = LocalContext.current
            PreferenceHelper.getLoggedInUsernameUser(context)?.let {
                Text(text = it)
            }
            Text(text = "Age")
            Text(text = response.age)
            Text(text = "Location")
            Text(text = response.location)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {



}

