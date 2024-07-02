package com.example.reminder.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun getRemindertime(date: String, time: String): Long {

    val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))

    val day = String.format("%02d", localDate.dayOfMonth)
    val month = String.format("%02d", localDate.monthValue)
    val hour = String.format("%02d", localTime.hour)
    val minute = String.format("%02d", localTime.minute)

    val strDT = "${localDate.year}-${month}-${day}T${hour}:${minute}:00.000Z"
    val meetingTime: Instant = Instant.parse(strDT)

    val oneHourInMillis = 60 * 60 * 1000
    val reminderTime = meetingTime.toEpochMilli() - oneHourInMillis

    return reminderTime
}