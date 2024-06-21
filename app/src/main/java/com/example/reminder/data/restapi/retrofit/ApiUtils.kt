package com.example.reminder.data.restapi.retrofit

class ApiUtils {
    companion object {
        private const val BASE_URL = "https://randomuser.me/"

        fun getInterfaceDao(): UserDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(UserDaoInterface::class.java)
        }
    }
}
