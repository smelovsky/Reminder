package com.example.reminder.data.restapi.models

import com.google.gson.annotations.SerializedName

data class RandomUser(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<Result>
)
