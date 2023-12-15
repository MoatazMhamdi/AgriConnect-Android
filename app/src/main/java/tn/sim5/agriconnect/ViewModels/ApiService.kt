package tn.sim5.agriconnect.ViewModels

import ClientSignUp
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tn.sim5.agriconnect.models.ClientSignUpRequest
import tn.sim5.agriconnect.models.ClientSignUpResponse
import tn.sim5.agriconnect.models.DeleteUserRequest
import tn.sim5.agriconnect.models.DeleteUserResponse
import tn.sim5.agriconnect.models.EditProfileRequest
import tn.sim5.agriconnect.models.EditProfileResponse
import tn.sim5.agriconnect.models.FarmerSignUpRequest
import tn.sim5.agriconnect.models.FarmerSignUpResponse
import tn.sim5.agriconnect.models.ForgetPasswordRequest
import tn.sim5.agriconnect.models.LoginRequest
import tn.sim5.agriconnect.models.LoginResponse
import tn.sim5.agriconnect.models.OtpResponse
import tn.sim5.agriconnect.models.OtpResponsee
import tn.sim5.agriconnect.models.ResetPasswordRequest
import tn.sim5.agriconnect.models.ResetPasswordResponse
import tn.sim5.agriconnect.models.VerifyOtpRequest

interface ApiService {
    @POST("users/FarmerSignup") // Replace with your actual API endpoint
    fun signUpFarmer(@Body request: FarmerSignUpRequest): Call<FarmerSignUpResponse>
    @POST("users/ClientSignup") // Replace with your actual API endpoint
    fun signUpClient(@Body request: ClientSignUpRequest): Call<ClientSignUpResponse>
    @POST("users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
    @PATCH("users/editProfile")
    fun editProfile(@Header("Authorization") token: String, @Body request: EditProfileRequest): Call<EditProfileResponse>
    @POST("users/forgetPassword")
    suspend fun forgetPassword(@Body request: ForgetPasswordRequest): Response<OtpResponse>
    @POST("users/verifyOTP")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<OtpResponsee>
    @POST("users/resetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>
    @DELETE("users/{numTel}")
    suspend fun deleteUserByNumTel(@Path("numTel") numTel: String): Response<DeleteUserResponse>

}