package com.app.bookingapp.presentation

import java.text.SimpleDateFormat
import java.util.*

object Helper {
    val dateFormatterShow = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    fun getTimeMilesFromDate(date: String): Long =
        dateFormatterShow.parse(date)!!.time

}