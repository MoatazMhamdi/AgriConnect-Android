package tn.sim5.agriconnect.utils


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.sim5.agriconnect.ui.MaintenanceApiService

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
    val apiM: MaintenanceApiService by lazy {
        retrofit.create(MaintenanceApiService::class.java)
    }
    val apiBlog: BlogApi by lazy {
        retrofit.create(BlogApi::class.java)
    }

}
