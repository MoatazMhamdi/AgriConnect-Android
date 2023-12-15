import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.models.ForgetPasswordRequest
import tn.sim5.agriconnect.utils.RetrofitImp

class ForgetPasswordViewModel : ViewModel() {

    private val apiService = RetrofitImp.apiService

    private val _otpResult = MutableLiveData<String>()
    val otpResult: LiveData<String> get() = _otpResult

    fun forgetPassword(numTel: String) {
        viewModelScope.launch {
            try {
                // Create a ForgetPasswordRequest object
                val forgetPasswordRequest = ForgetPasswordRequest(numTel)

                // Call the API with the request object
                val response = apiService.forgetPassword(forgetPasswordRequest)

                if (response.isSuccessful) {
                    val otp = response.body()?.otp
                    _otpResult.value = otp ?: "Error: OTP is null"
                } else {
                    _otpResult.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _otpResult.value = "Error: ${e.message}"
            }
        }
    }
}