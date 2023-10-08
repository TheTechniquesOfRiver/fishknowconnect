package com.example.fishknowconnect.ui.selectLanguage

import LocaleHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fishknowconnect.ui.NavigationDrawerActivity
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.login.LoginActivity
import com.example.fishknowconnect.ui.selectLanguage.ui.theme.FishKnowConnectTheme

class SelectLanguage : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background,
                ) {
                   if(PreferenceHelper.getInstance(applicationContext).getUserLoggedInStatus()){
                       val activity = (LocalContext.current as? Activity)
                       val intent = Intent(activity, NavigationDrawerActivity::class.java)
                       activity?.startActivity(intent)
                       activity?.finish()
                   }else{
                       SelectLanguageOption()
                   }
                }
            }
        }
    }
}

@Composable
fun SelectLanguageOption() {
    val mContext = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    val languageOptions =
        listOf(stringResource(R.string.radio_english), stringResource(R.string.radio_bangala))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(languageOptions[1]) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.selectableGroup().padding(0.dp,50.dp)
    ) {
        Text(
            text = "Please select language/ দয়া  করে  ভাষা পছন্দ করুন ",
            style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        languageOptions.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(selected = (text == selectedOption), onClick = {
                        onOptionSelected(text)
                        //set language
                        if (text == languageOptions[1]) {
                            LocaleHelper.setLocale(mContext, "bn");
                        } else {
                            LocaleHelper.setLocale(mContext, "en");
                        }
                        val intent = Intent(activity, LoginActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.finish()
                    })
                    .padding(horizontal = 16.dp)
            ) {
                    RadioButton(selected = (text == selectedOption), onClick = null,)
                    Text(
                        text = text,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, 7.dp)
                    )

            }
        }
    }
}

/**
 * Preview of Select language Screen
 */

@Preview(showBackground = true)
@Composable
fun SelectLanguageOptionPreview() {
    FishKnowConnectTheme {
        SelectLanguageOption()
    }
}