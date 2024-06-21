package com.example.reminder.data.restapi.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Dob(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: Date
)
