package tn.sim5.agriconnect.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.sim5.agriconnect.models.EditProfileRequest
import tn.sim5.agriconnect.models.EditProfileResponse
import tn.sim5.agriconnect.models.Result
import tn.sim5.agriconnect.utils.RetrofitImp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val apiService = RetrofitImp.apiService

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
}
