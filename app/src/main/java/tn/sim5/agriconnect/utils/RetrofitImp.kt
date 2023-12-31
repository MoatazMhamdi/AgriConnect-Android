package tn.sim5.agriconnect.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.sim5.agriconnect.ViewModels.ApiService

object RetrofitImp {
    //private const val BASE_URL = "http://10.0.2.2:9090/"
    private const val BASE_URL = "http://10.0.2.2:9090/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }




    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}