package tn.sim5.agriconnect.ui

import tn.sim5.agriconnect.models.Maintenance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MaintenanceApiService {

    @GET("maintenances/equipments/{id}")
    fun getMaintenanceByEquipmentId(@Path("id") equipmentId: String): Call<List<Maintenance>>
}