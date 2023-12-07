package com.example.bicycles.service

import com.example.bicycles.models.Equipment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface EquipmentApiService {
    @GET("equipments")
    fun getAllEquipments(): Call<List<Equipment>>

    @GET("equipments/{id}")
    fun getEquipmentById(@Path("id") equipmentId: String): Call<Equipment>

    @Multipart
    @POST("equipments")
    fun createEquipmentWithImage(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("categorie") categorie: RequestBody,
        @Part("etat") etat: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Equipment>

    @Multipart
    @PUT("equipments/{id}")
    fun updateEquipmentWithImage(
        @Path("id") equipmentId: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("categorie") categorie: RequestBody,
        @Part("etat") etat: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Equipment>


    @DELETE("equipments/{id}")
    fun deleteEquipment(@Path("id") equipmentId: String): Call<Unit>

    @GET("equipments/user/{userId}")
    fun getEquipmentByUserId(@Path("userId") userId: String): Call<List<Equipment>>








}
