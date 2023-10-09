package com.example.fishknowconnect.ui.privatePost

import android.app.Activity
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.R

/**
 * Display all private post list
 */
@Composable
fun DisplayPrivateList(
    list: List<GetPrivatePostResponse>,
    context: Activity?,
    viewModel: PrivatePostViewModel
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            item.author,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
//            viewModel.sendRequestToAccessPost(item._id)
        }) {
            Text(text = stringResource(id = R.string.text_request_access))
        }
    }
    Divider()
}

