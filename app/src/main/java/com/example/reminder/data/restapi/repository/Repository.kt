package com.example.reminder.data.restapi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.reminder.UserDetails
import com.example.reminder.data.restapi.retrofit.ApiUtils
import com.example.reminder.data.restapi.retrofit.UserDaoInterface
import com.example.reminder.mainViewModel

class Repository {

    val isLoading = MutableLiveData<Boolean>()
    val isErrorOccurred = MutableLiveData<Boolean>()
    private var dif: UserDaoInterface = ApiUtils.getInterfaceDao()

    suspend fun getRandomUser() {
        try {
            isLoading.value = true
            isErrorOccurred.value = false
            val response = dif.getRandomUser()
            if (response.isSuccessful) {

                val user = response.body()!!.results[0]

                val userDetails = UserDetails (
                    //id = user.id.value.toInt(),
                    id = mainViewModel.userListEntity.size,
                    name = "${user.name.first} ${user.name.last}",
                    email = user.email,
                    picture_thumbnail = user.picture.thumbnail,
                    picture_large = user.picture.large
                    )
                mainViewModel.userListEntity += userDetails

                //Log.d("zzz", "${userInfoData.value}")
                isLoading.value = false
                isErrorOccurred.value = false
            }
        } catch (t: Throwable) {
            t.localizedMessage?.toString()?.let { Log.e("zzz", "error: ${it}") }
            isLoading.value = false
            isErrorOccurred.value = true
        }
    }
}