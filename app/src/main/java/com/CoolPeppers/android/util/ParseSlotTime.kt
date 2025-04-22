package com.CoolPeppers.android.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ParseSLotTime(slotTime: String): Pair<String, String> {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val dateTime = LocalDateTime.parse(slotTime,inputFormatter)

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale("ru"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date = dateTime.format(dateFormatter)
    val time = dateTime.format(timeFormatter)

    return date to time
}