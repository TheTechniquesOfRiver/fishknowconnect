package com.example.fishknowconnect.ui.privatePost

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.CustomFullWidthIconButton
import com.example.fishknowconnect.ui.CustomWrapWidthIconButton
import com.example.fishknowconnect.util.getFormattedDate

/**
 * Display all private post list
 */
@Composable
fun DisplayPrivateList(
    list: List<GetPrivatePostResponse>, context: Activity?, viewModel: PrivatePostViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(list) { post ->
                ListItem(post, context, viewModel)
            }
        }
    }
}

/**
 * Each list item
 */
@Composable
fun ListItem(item: GetPrivatePostResponse, context: Activity?, viewModel: PrivatePostViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    )
    {
        Text(
            stringResource(id = R.string.text_posted_by)+ " "+item.author,
            maxLines = 1,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            getFormattedDate(item.timestamp),
            fontStyle = FontStyle.Italic,
            maxLines = 1,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            item.title,
            maxLines = 1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomWrapWidthIconButton(
            label = stringResource(id = R.string.text_sent_request), icon = R.drawable.icon_send
        ) {
            //hit api to send request access
            viewModel.sendRequestToAccessPost(item._id)
        }
    }
    Divider()
}

