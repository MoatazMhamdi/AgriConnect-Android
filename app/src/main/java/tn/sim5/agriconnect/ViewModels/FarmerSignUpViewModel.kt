import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.models.ClientSignUpRequest
import tn.sim5.agriconnect.models.FarmerSignUpRequest
import tn.sim5.agriconnect.models.FarmerSignUpResponse
import tn.sim5.agriconnect.utils.RetrofitImp

class FarmerSignUpViewModel : ViewModel() {

    private val _signUpResult = MutableLiveData<String>()
    val signUpResult: LiveData<String> get() = _signUpResult

    private val apiService = RetrofitImp.apiService

    fun signUpFarmer(name: String, email: String, password: String, numTel: String) {
        val request = FarmerSignUpRequest(name, email, password, numTel)
        val call = apiService.signUpFarmer(request)

        call.enqueue(object : Callback<FarmerSignUpResponse> {
            override fun onResponse(call: Call<FarmerSignUpResponse>, response: Response<FarmerSignUpResponse>) {
                when {
                    response.isSuccessful -> {
                        val message = response.body()?.message ?: "Unknown message"
                        _signUpResult.value = message
                    }
                    response.code() == 400 -> {
                        // Handle 400 Bad Request
                        _signUpResult.value = "User already exists or invalid request"
                    }
                    else -> {
                        // Handle other error cases
                        _signUpResult.value = "Sign up failed"
                    }
                }
            }

            override fun onFailure(call: Call<FarmerSignUpResponse>, t: Throwable) {
                // Handle failure
                _signUpResult.value = "Sign up failed"
            }
        })
    }

}