package com.example.fishknowconnect.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.R

/**
 * progress bar
 */
@Composable
fun IndeterminateCircularIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            trackColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

/**
 * shows error dialog
 */
@Composable
fun ShowErrorMessage() {
    val context = LocalContext.current as? Activity
    Toast.makeText(
        context, stringResource(id = R.string.text_something_went_wrong), Toast.LENGTH_SHORT
    ).show()
}


/**
 *tool bar design set up
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBarLayout(title: String) {
    val activity = (LocalContext.current as? Activity)
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color(
            0xFF70ced3
        )
    ), title = {
        Text(
            text = title, style = TextStyle(color = Color.White, fontSize = 22.sp)
        )
    }, navigationIcon = {
        IconButton(onClick = {
            activity?.finish()
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Menu",
                tint = Color.White
            )
        }
    })
}

/**
 *common button design
 */
@Composable
fun CustomFullWidthIconButton(
    label: String, icon: Int, onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick, // Handle click using the onClick parameter
        modifier = Modifier
            .padding(8.dp)
            .height(50.dp)
            .fillMaxWidth(),

    ) {
        Text(
            text = label,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.width(30.dp))
        Image(
            modifier = Modifier
                .width(76.dp)
                .height(46.dp)
                .padding(2.dp),
            painter = painterResource(icon),
            contentDescription = ""
        )
    }

}
