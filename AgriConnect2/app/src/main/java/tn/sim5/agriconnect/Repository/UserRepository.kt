package tn.sim5.agriconnect.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.ViewModels.ApiService
import tn.sim5.agriconnect.models.EditProfileRequest
import tn.sim5.agriconnect.models.EditProfileResponse
import tn.sim5.agriconnect.models.LoginRequest
import tn.sim5.agriconnect.models.LoginResponse
import tn.sim5.agriconnect.models.Result

class UserRepository(private val apiService: ApiService) {

    fun editProfile(token: String, userId: String, fieldsToUpdate: Map<String, String>): LiveData<Result<EditProfileResponse>> {
        val liveData = MutableLiveData<Result<EditProfileResponse>>()

        val request = EditProfileRequest(userId = userId, fieldsToUpdate = fieldsToUpdate)

        apiService.editProfile("Bearer $token", request).enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(call: Call<EditProfileResponse>, response: Response<EditProfileResponse>) {
                if (response.isSuccessful) {
                    liveData.value = Result.Success(response.body())
                } else {
                    liveData.value = Result.Error(Exception("Failed to edit profile"))
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                liveData.value = Result.Error(t)
            }
        })

        return liveData
    }
    fun login(numTel: String, password: String): LiveData<Result<LoginResponse>> {

        val liveData = MutableLiveData<Result<LoginResponse>>()
        val request = LoginRequest(numTel, password)

        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    liveData.value = Result.Success(response.body())
                } else {
                    liveData.value = Result.Error(Exception("Login failed"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                liveData.value = Result.Error(t)
            }
        })

        return liveData
    }
}
