package com.example.xpensate

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatToDecimalValue(d:Double):String{
        return String.format("%.2f",d)
    }

    fun getMillisFromDate(date: Long): Long {
        return getMilliFromDate(date)
    }
    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun getMilliFromDate(dateFormat: Long): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
    }

    fun getMonthYear(dateMillis: Long): String {
        val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return formatter.format(dateMillis)
    }
}