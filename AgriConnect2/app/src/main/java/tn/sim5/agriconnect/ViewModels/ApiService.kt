package tn.sim5.agriconnect.ViewModels

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import tn.sim5.agriconnect.models.FarmerSignUpRequest
import tn.sim5.agriconnect.models.FarmerSignUpResponse

interface ApiService {
    @POST("users/FarmerSignup") // Replace with your actual API endpoint
    fun signUpFarmer(@Body request: FarmerSignUpRequest): Call<FarmerSignUpResponse>
}
