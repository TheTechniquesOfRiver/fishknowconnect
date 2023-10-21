package com.example.fishknowconnect.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getFormattedDate(timestamp: String): String {
    val formatter = SimpleDateFormat("EEE,dd MMM yyyy hh:mm:ss zzz", Locale.ENGLISH)
    val date: Date = formatter.parse(timestamp)
    val newFormatter = SimpleDateFormat("EEE,dd MMM yyyy hh:mm", Locale.getDefault())
    return newFormatter.format(date)
}