package com.example.fishknowconnect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.ui.GetPostTypeResponse
import com.example.fishknowconnect.ui.contentDetail.ContentDetailActivity
import com.example.fishknowconnect.ui.newPost.ShowAudioPlayer
import com.example.fishknowconnect.ui.newPost.ShowVideoPlayer
import com.example.fishknowconnect.util.getFormattedDate


/**
 * Display all list
 */
@Composable
fun DisplayList(list: List<GetPostTypeResponse>, title: String) {
    val context = LocalContext.current
    //title
    Text(
        text = title, style = TextStyle(
            fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp), textAlign = TextAlign.Center
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(list) { post ->
                ListItem(post, context)
            }
        }
    }
}

/**
 * Each list item
 */
@Composable
fun ListItem(item: GetPostTypeResponse, context: Context) {
    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.app_status_bar_light),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { openItemDetailScreen(item, context) },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                item.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontFamily = FontFamily.SansSerif,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                item.content,
                fontSize = 16.sp,
                maxLines = 2,
                fontFamily = FontFamily.SansSerif,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(id = R.string.text_posted_by) + " " + item.author,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
               fontStyle = FontStyle.Italic,
                maxLines = 1,
                fontFamily = FontFamily.SansSerif,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                getFormattedDate(item.timestamp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                fontFamily = FontFamily.SansSerif,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            )
        }
    }
}


/**
 * Open detail screen with each item
 */
fun openItemDetailScreen(item: GetPostTypeResponse, context: Context) {
    Log.d("id", "id" + item._id)
    val intent = Intent(context, ContentDetailActivity::class.java).apply {
        putExtra("title", item.title)
        putExtra("content", item.content)
        putExtra("file_url", item.file_url)
        putExtra("fileType", item.fileType)
        putExtra("_id", item._id)
        putExtra("author", item.author)
    }
    context.startActivity(intent)
}
