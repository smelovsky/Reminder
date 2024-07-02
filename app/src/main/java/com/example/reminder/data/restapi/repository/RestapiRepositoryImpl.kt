package com.example.reminder.data.restapi.repository

import androidx.lifecycle.MutableLiveData
import com.example.reminder.UserDetails
import com.example.reminder.data.restapi.retrofit.ApiUtils
import com.example.reminder.data.restapi.retrofit.UserDaoInterface

class RestapiRepositoryImpl : RestapiRepositoryApi {

    val isLoading = MutableLiveData<Boolean>()
    val isErrorOccurred = MutableLiveData<Boolean>()
    private var userDaoInterface: UserDaoInterface = ApiUtils.getInterfaceDao()

    override suspend fun getRandomUser(index: Int) : UserDetails {

        var userDetails = UserDetails(index, "", "", "", "")

        try {
            isLoading.value = true
            isErrorOccurred.value = false
            val response = userDaoInterface.getRandomUser()
            if (response.isSuccessful) {

                val user = response.body()!!.results[0]

                userDetails = UserDetails (
                    id = index,
                    name = "${user.name.first} ${user.name.last}",
                    email = user.email,
                    picture_thumbnail = user.picture.thumbnail,
                    picture_large = user.picture.large
                )

                isLoading.value = false
                isErrorOccurred.value = false
            }
        } catch (t: Throwable) {
            isLoading.value = false
            isErrorOccurred.value = true
        }

        return userDetails

    }
}
