package com.example.hataru.data

import com.example.hataru.BuildConfig
import com.example.hataru.domain.FlatsRep
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Root
import com.example.hataru.domain.entity.UserCredentials
import com.example.hataru.domain.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FlatsImpl(private val ser: ApiService) : FlatsRep {

    override suspend fun getFlats(): List<Roomtype> {
        val username = BuildConfig.USERNAME_KEY
        val password = BuildConfig.PASSWORD_KEY

        return withContext(Dispatchers.IO) {

            val authenticationResponse = ser.authenticateUser(
                credentials = UserCredentials(username, password)
            ).execute()

            if (authenticationResponse.isSuccessful) {

                val roomTypeResponse = ser.getRoomTypes().execute()
                if (roomTypeResponse.isSuccessful) {
                    return@withContext (roomTypeResponse.body() as Root).roomtypes!!.toList()

                } else {
                    throw Exception()
                }
            } else {
                throw Exception()
            }
        }
    }


}


