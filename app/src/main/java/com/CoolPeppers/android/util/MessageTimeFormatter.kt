package com.CoolPeppers.android.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object MessageTimeFormatter {

    fun formatSmartDateTime(isoTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val messageDate = inputFormat.parse(isoTime) ?: return isoTime
            val now = Calendar.getInstance()
            val calendar = Calendar.getInstance().apply { time = messageDate }

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val monthFormat = SimpleDateFormat("d MMM", Locale.getDefault())
            val fullFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

            when {
                // Сегодня
                now.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                        now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) -> timeFormat.format(messageDate)

                // До 6 дней назад в текущем году
                (now.timeInMillis - messageDate.time < 6 * 24 * 60 * 60 * 1000) &&
                        (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) -> dayFormat.format(messageDate)

                // Тот же год
                now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) -> monthFormat.format(messageDate)

                // Разные годы
                else -> fullFormat.format(messageDate)
            }
        } catch (e: Exception) {
            isoTime
        }
    }
    fun formatTimeOnly(isoTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val date = if (isoTime.contains("T")) {
                inputFormat.parse(isoTime)
            } else {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(isoTime)
            }
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date ?: return isoTime)
        } catch (e: Exception) {
            isoTime
        }
    }
    fun formatDate(isoTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val date = inputFormat.parse(isoTime) ?: return isoTime
            SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            isoTime
        }
    }
}