package com.example.bicycles.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:9090/" // Remplacez ceci par votre URL de base

    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: EquipmentApiService by lazy {
        retrofit.create(EquipmentApiService::class.java)
    }

    val uploadImage: EquipmentApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        retrofit.create(EquipmentApiService::class.java)
    }
}
