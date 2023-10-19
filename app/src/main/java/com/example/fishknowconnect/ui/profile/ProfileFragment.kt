package com.example.fishknowconnect.ui.profile

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fishknowconnect.DisplayList
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.FragmentProfileBinding
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.contentDetail.isListenEnable
import com.example.fishknowconnect.ui.contentDetail.tts
import com.example.fishknowconnect.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    lateinit var profileViewModelFactory: ProfileViewModelFactory
    private var _binding: FragmentProfileBinding? = null
    lateinit var preferenceHelper: PreferenceHelper
    val profileViewModel: ProfileViewModel by viewModels(factoryProducer = { profileViewModelFactory })

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        preferenceHelper = PreferenceHelper.getInstance(requireContext())
        profileViewModelFactory = ProfileViewModelFactory(
            preferenceHelper, FishKnowConnectApi.retrofitService
        )
        binding.composeViewProfile.apply {
            setContent {
                LaunchedEffect(Unit, block = {
                    profileViewModel.getProfileInfo()

                })
                //title text
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = profileViewModel.username,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(7f),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        //logout button
                        OutlinedButton(onClick = {
                            performLogout()
                        }) {
                            Image(
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .height(IntrinsicSize.Min),
                                painter = painterResource(R.drawable.exit),
                                contentDescription = ""
                            )
                        }
                    }
                    ProfileScreen(profileViewModel)
                }
            }
        }
        val root: View = binding.root
        return root
    }

    /**
     * Performs logout
     */
    private fun performLogout() {
        preferenceHelper.setUserLoggedInStatus(false)
        preferenceHelper.setLoggedInUserUsername("")
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfilePosts()

    }
}


@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    //for profile information
    when (val response = profileViewModel.state.collectAsState().value) {
        ProfileState.Loading -> IndeterminateCircularIndicator()
        is ProfileState.Success -> SetProfileInfo(response)
        is ProfileState.Error -> showDialog(response.message)
        else -> {
        }
    }
    //for profile post list
    when (val responseValue = profileViewModel.stateProfile.collectAsState().value) {
        ProfilePostListPostState.Loading -> IndeterminateCircularIndicator()
        is ProfilePostListPostState.Success -> responseValue.response?.let {
            DisplayList(it, stringResource(id = R.string.text_my_post))
        }

        is ProfilePostListPostState.Failure -> showDialog(responseValue.response)
        else -> {
        }
    }
}


/**
 * shows error dialog
 */
@Composable
fun showDialog(message: String) {
    Log.d("register", message)
    Toast.makeText(LocalContext.current.applicationContext, message, Toast.LENGTH_SHORT).show();
}

/**
 * Set profile information
 */
@Composable
fun SetProfileInfo(response: ProfileState.Success) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp, 0.dp, 4.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = response.age,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal),modifier = Modifier.padding(horizontal = 2.dp)
            )
            Text(
                text = stringResource(id = R.string.text_years),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal),modifier = Modifier.padding(horizontal = 2.dp)
            )
            Text(
                text = response.location,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                ),modifier = Modifier.padding(horizontal = 2.dp)
            )
//            Text(
//                text = stringResource(id = R.string.text_age), style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                ), modifier = Modifier.padding(horizontal = 10.dp)
//            )
//            Text(
//                text = response.age,
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.SemiBold,
//                ),
//                modifier = Modifier
//                    .padding(horizontal = 10.dp)
//                    .weight(8f),
//            )
//        }
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(16.dp, 0.dp, 0.dp, 8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = stringResource(id = R.string.text_location), style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                ), modifier = Modifier.padding(horizontal = 10.dp)
//            )
//            Text(
//                text = response.location,
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.SemiBold,
//                ),
//                modifier = Modifier
//                    .padding(horizontal = 10.dp)
//                    .weight(8f),
//            )
        }
    }
}

