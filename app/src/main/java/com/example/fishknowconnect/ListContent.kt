package com.example.fishknowconnect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.ui.fish.Fish
import com.example.fishknowconnect.ui.fish.FishDataProvider


/**
 * show list of the latest post
 */
@Composable
fun ListContent() {
    val fishes = remember { FishDataProvider.fishList }
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)) {
        items(items = fishes, itemContent = {
            FishListItem(fish = it)
        })
    }
}

/**
 * Each list item
 */
@Composable
private fun FishListItem(fish: Fish) {
    Row {
        Column {
            Text(text = fish.title)
            Text(text = fish.description)
        }
    }
}
