package com.example.loopcongo.restApi

import com.example.loopcongo.models.User
import com.example.loopcongo.models.UserUniqueResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiClient {
    private const val BASE_URL = "https://loopcongo.com/api/"

    val instance: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Fonction pour mettre a jour les données de l'utilisateur connecté en local
    /*suspend fun updateUserConnectedData(userId: Int): User? = withContext(Dispatchers.IO) {
        try {
            val response = instance.getUserById(userId).execute()
            if (response.isSuccessful && response.body()?.status == true) {
                response.body()?.data
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

}
