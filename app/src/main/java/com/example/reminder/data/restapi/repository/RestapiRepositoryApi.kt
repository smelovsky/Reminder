package com.example.reminder.data.restapi.repository

import com.example.reminder.UserDetails

interface RestapiRepositoryApi {

    suspend fun getRandomUser(index: Int) : UserDetails
}