package com.example.fishknowconnect.ui.selectLanguage

import LocaleHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.login.LoginActivity
import com.example.fishknowconnect.ui.register.RegisterActivity
import com.example.fishknowconnect.ui.selectLanguage.ui.theme.FishKnowConnectTheme
import java.util.Locale

class SelectLanguage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Log.d("Locale", ""+ Locale.getDefault())

                    SelectLanguageOption()
                }
            }
        }
    }
}

@Composable
fun SelectLanguageOption() {
    val mContext = LocalContext.current
    val languageOptions =
        listOf(stringResource(R.string.radio_english), stringResource(R.string.radio_bangala))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(languageOptions[1]) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.selectableGroup()) {
        Text(
            text = "Please select language",
            style = MaterialTheme.typography.bodyLarge.merge(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        languageOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(selected = (text == selectedOption), onClick = {
                        onOptionSelected(text)
                        //set language
                        if (text == languageOptions[1]) {
                            LocaleHelper.setLocale(mContext, "bn");
                        } else {
                            LocaleHelper.setLocale(mContext, "en");
                        }
                        //start register screen
                        val i = Intent(mContext, RegisterActivity::class.java)
                        mContext.startActivity(i)
                    })
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(selected = (text == selectedOption), onClick = null)
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.merge(),
                    modifier = Modifier.padding(horizontal = 16.dp)
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