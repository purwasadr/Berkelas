package com.alurwa.berkelas.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Purwa Shadr Al 'urwa on 22/07/2021
 */

object DateTimeUtil {
    fun convertDateMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun convertDateStringToMillis(dateString: String): Long? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = formatter.parse(dateString)?.time
            date
        } catch (ex: ParseException) {
            null
        }
    }
}