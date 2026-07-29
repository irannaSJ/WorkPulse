package com.example.workpulse.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object TimeUtils {

    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    fun formatTime(time: Long?): String {

        if (time == null) return "--:--"

        return Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(timeFormatter)
    }

    fun formatDuration(seconds: Long): String {

        val hours = TimeUnit.SECONDS.toHours(seconds)

        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60

        val remainingSeconds = seconds % 60

        return String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            remainingSeconds
        )
    }
}