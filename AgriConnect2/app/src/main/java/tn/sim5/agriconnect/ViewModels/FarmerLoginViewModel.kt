package tn.sim5.agriconnect.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.models.FarmerSignUpResponse
import tn.sim5.agriconnect.models.LoginRequest
import tn.sim5.agriconnect.models.LoginResponse
import tn.sim5.agriconnect.utils.RetrofitImp
import tn.sim5.agriconnect.utils.SessionManager

class FarmerLoginViewModel: ViewModel() {
    private lateinit var context: Context
    private val sessionManager: SessionManager by lazy { SessionManager(context) }
    private val _loginResult = MutableLiveData<String>()
    private val _userExistsResult = MutableLiveData<Boolean>()
    val userExistsResult: LiveData<Boolean> get() = _userExistsResult
    val loginResult: LiveData<String> get() = _loginResult
    private val _jwtToken = MutableLiveData<String>()
    val jwtToken: LiveData<String> get() = _jwtToken
    private val apiService = RetrofitImp.apiService
// observe live data name, email
    private val _userFullName = MutableLiveData<String>()
    private val _userEmail = MutableLiveData<String>()
    val userFullName: LiveData<String> get() = _userFullName
    val userEmail: LiveData<String> get() = _userEmail
    fun init(context: Context) {
        this.context = context
    }
    fun loginFarmer(numTel: String, password: String) {
        val request = LoginRequest(numTel, password)
        val call = apiService.loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val message = loginResponse.message ?: "Unknown message"
                        _loginResult.value = message
                        _userFullName.value = loginResponse.name
                        _userEmail.value = loginResponse.email

                        // Handle JWT token
                        val jwtToken = loginResponse.token
                        _jwtToken.value = jwtToken
                        val userId = loginResponse.userId
                        saveTokenAndUserIdToSharedPreferences(jwtToken, userId)
                        // Save JWT token to SharedPreferences
                        saveTokenToSharedPreferences(jwtToken)
                        sessionManager.setAuthenticationStatus(true)


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
    private fun saveTokenAndUserIdToSharedPreferences(token: String, userId: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("JWT", token)
        editor.putString("USER_ID", userId)
        editor.apply()
    }
    // Save JWT token to SharedPreferences
    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("JWT", token)
        editor.apply()
    }


    // Check if the user is authenticated
    fun isAuthenticated(): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val storedToken = sharedPreferences.getString("JWT", null)
        return !storedToken.isNullOrEmpty()
    }

    // Clear session on logout
    fun clearSession() {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("JWT")
        editor.apply()
    }
    fun setUserDetails(name: String, email: String) {
        _userFullName.value = name
        _userEmail.value = email
    }
    fun getUserFullName(): String? {
        return userFullName.value
    }

    fun getUserEmail(): String? {
        return userEmail.value
    }
}
