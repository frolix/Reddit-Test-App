package com.example.reddittestapp.util

import java.util.*

fun Long.toTimeAgo(): String {
    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS

    val time = Date(this * 1000).time
    val now = Calendar.getInstance().time.time

    val different = now - time
    return when {
        different < MINUTE_MILLIS -> "now"
        different < 2 * MINUTE_MILLIS -> "a minute ago"
        different < 60 * MINUTE_MILLIS -> "${different / MINUTE_MILLIS} minutes ago"
        different < 2 * HOUR_MILLIS -> "an hour ago"
        different < 24 * HOUR_MILLIS -> "${different / HOUR_MILLIS} hours ago"
        different < 48 * HOUR_MILLIS -> "yesterday"
        else -> "${different / DAY_MILLIS} days ago"
    }
}

