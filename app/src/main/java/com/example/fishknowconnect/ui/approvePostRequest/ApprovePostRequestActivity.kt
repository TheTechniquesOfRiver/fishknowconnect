package com.example.fishknowconnect.ui.approvePostRequest

import LocaleHelper
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.CustomFullWidthIconButton
import com.example.fishknowconnect.ui.CustomWrapWidthIconButton
import com.example.fishknowconnect.ui.IndeterminateCircularIndicator
import com.example.fishknowconnect.ui.showError
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.newPost.ui.theme.FishKnowConnectTheme
import java.util.Arrays

/**
 * approves post request for private posts
 */
class ApprovePostRequestActivity : ComponentActivity() {
    private lateinit var approvePostRequestViewModelFactory: ApprovePostRequestViewModelFactory
    lateinit var preferenceHelper: PreferenceHelper
    val viewModel: ApprovePostRequestViewModel by viewModels(factoryProducer = { approvePostRequestViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper.getInstance(this)
        approvePostRequestViewModelFactory = ApprovePostRequestViewModelFactory(
            preferenceHelper, FishKnowConnectApi.retrofitService
        )
        setContent {
            FishKnowConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val context = LocalContext.current
                        ToolBarLayout(getString(R.string.textview_approve_post_request))
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            ApproveState.Loading -> IndeterminateCircularIndicator()
                            is ApproveState.Success -> responseValue.response?.let {
                                ApprovePostScreen(it, viewModel)
                            }
                            is ApproveState.Error -> showError()
                            else -> {
                            }
                        }
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            ApproveState.Loading -> IndeterminateCircularIndicator()
                            is ApproveState.SuccessGrant -> responseValue.response?.let {
                                Toast.makeText(
                                    context,
                                    stringResource(id = R.string.text_request_approved),
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }

                            is ApproveState.Error -> showError()
                            else -> {
                            }
                        }
                        when (val responseValue = viewModel.state.collectAsState().value) {
                            ApproveState.Loading -> IndeterminateCircularIndicator()
                            is ApproveState.RejectGrant -> responseValue.response?.let {
                                Toast.makeText(
                                    context,
                                    stringResource(id = R.string.text_request_rejected),
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            is ApproveState.Error -> showError()
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * locale attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onResume() {
        super.onResume()
        viewModel.getApprovals()
    }
}

/**
 * Each list item
 */
@Composable
fun ListItem(item: GetApprovalResponse, viewModel: ApprovePostRequestViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            item.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = TextStyle(
                fontSize = 22.sp, fontWeight = FontWeight.Medium
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    //for each request user
    val requestedString = item.requested
    val requestedList =
        Arrays.asList(*requestedString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
    requestedList.forEach { approvalRequestUser ->
        CustomWrapWidthIconButton(
            label = stringResource(id = R.string.text_approve_access) + " $approvalRequestUser",
            icon = R.drawable.icon_approve
        ) {
            //hit api to send post approval
            viewModel.sendPostApproval(item._id, approvalRequestUser)
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomWrapWidthIconButton(
            label = stringResource(id = R.string.text_reject_post) + " $approvalRequestUser",
            icon = R.drawable.icon_reject
        ) {
            //hit api to reject post approval
            viewModel.sendRejectApproval(item._id, approvalRequestUser)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    Divider()
}

/**
 * Approve screen
 */
@Composable
fun ApprovePostScreen(list: List<GetApprovalResponse>, viewModel: ApprovePostRequestViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            items(list) { post ->
                ListItem(post, viewModel)
            }
        }
    }
}
