package com.example.reminder.data.restapi.retrofit

import com.example.reminder.data.restapi.models.RandomUser
import retrofit2.Response
import retrofit2.http.GET

interface UserDaoInterface {

    @GET("api")
    suspend fun getRandomUser(): Response<RandomUser>

}