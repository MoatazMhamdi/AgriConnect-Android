// ResetPasswordViewModel.kt
package tn.sim5.agriconnect.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.models.ResetPasswordRequest
import tn.sim5.agriconnect.models.ResetPasswordResponse // Import the new class
import tn.sim5.agriconnect.utils.RetrofitImp

class ResetPasswordViewModel : ViewModel() {
    private val apiService = RetrofitImp.apiService

    private val _passwordReset = MutableLiveData<Boolean>()
    val passwordReset: LiveData<Boolean> get() = _passwordReset

    fun resetPassword(numTel: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val request = ResetPasswordRequest(numTel, newPassword)
                val response = apiService.resetPassword(request)

                // Assuming your API returns a message in the response body
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _passwordReset.value = true
                    // Access other properties if needed, e.g., responseBody.message
                } else {
                    _passwordReset.value = false
                }
            } catch (e: Exception) {
                _passwordReset.value = false
            }
        }
    }
}