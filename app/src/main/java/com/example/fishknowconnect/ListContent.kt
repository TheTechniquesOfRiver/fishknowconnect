package com.example.fishknowconnect

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.ui.fish.GetAllPostResponse
import com.example.fishknowconnect.ui.listItemDetail.ListItemDetailActivity


@Composable
fun DisplayList(list: List<GetAllPostResponse>, context: Activity?) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(list) { post ->
                ListItem(post,context)
            }
        }
    }
}

/**
 * Each list item
 */
@Composable
fun ListItem(item: GetAllPostResponse, context: Activity?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { openItemDetailScreen(item, context) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            item.content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
    }
    Divider()
}


fun openItemDetailScreen(item: GetAllPostResponse, context: Activity?) {
    val intent = Intent(context, ListItemDetailActivity::class.java).apply {
        putExtra("title",item.title)
        putExtra("content",item.content)
//        putExtra("file_url",item.fileUrl)
    }
    context?.startActivity(intent)
}
