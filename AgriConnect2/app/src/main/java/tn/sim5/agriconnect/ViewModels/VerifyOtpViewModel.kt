package tn.sim5.agriconnect.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.models.VerifyOtpRequest
import tn.sim5.agriconnect.utils.RetrofitImp

class VerifyOtpViewModel : ViewModel() {
    private val apiService = RetrofitImp.apiService

    private val _otpResult = MutableLiveData<Boolean>()
    val otpResult: LiveData<Boolean> get() = _otpResult

    fun verifyOtp(numTel: String, otp: String) {
        viewModelScope.launch {
            try {
                val request = VerifyOtpRequest(numTel, otp)
                val response = apiService.verifyOtp(request)

                Log.d("VerifyOtpViewModel", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    _otpResult.value = true
                } else {
                    _otpResult.value = false
                }
            } catch (e: Exception) {
                Log.e("VerifyOtpViewModel", "Error: ${e.message}")
                _otpResult.value = false
            }
        }
    }
}
