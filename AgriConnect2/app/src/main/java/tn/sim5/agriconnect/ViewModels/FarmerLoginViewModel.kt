package tn.sim5.agriconnect.ViewModels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.models.LoginRequest
import tn.sim5.agriconnect.models.LoginResponse
import tn.sim5.agriconnect.utils.RetrofitImp

class FarmerLoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _jwtToken = MutableLiveData<String>()
    val jwtToken: LiveData<String> get() = _jwtToken

    private val apiService = RetrofitImp.apiService

    fun loginFarmer(numTel: String, password: String, context: Context) {
        val request = LoginRequest(numTel, password)
        val call = apiService.loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val message = loginResponse.message ?: "Unknown message"
                        _loginResult.value = message

                        // Handle JWT token
                        val jwtToken = loginResponse.token
                        _jwtToken.value = jwtToken

                        // Save JWT token to SharedPreferences
                        saveTokenToSharedPreferences(jwtToken, context)
                    } else {
                        _loginResult.value = "Login response is null"
                    }
                } else {
                    _loginResult.value = "Login failed"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("FarmerLoginViewModel", "API call failed", t)
            }
        })
    }

    // Save JWT token to SharedPreferences
    private fun saveTokenToSharedPreferences(token: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("JWT", token)
        editor.apply()
    }

    // Check if the user is authenticated
    fun isAuthenticated(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val storedToken = sharedPreferences.getString("JWT", null)
        return !storedToken.isNullOrEmpty()
    }

    // Clear session on logout
    fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("JWT")
        editor.apply()
    }
}
