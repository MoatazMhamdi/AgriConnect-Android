package tn.sim5.agriconnect.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import tn.sim5.agriconnect.models.User

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MyPrefs",
        Context.MODE_PRIVATE
    )
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER = ""
    }
    var authToken: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(token) {
            editor.putString(KEY_TOKEN, token)
            editor.apply()
        }
    var user: User? = null
        set(value) {
            field = value
            saveUserDetails(value)
        }
        get() {
            val value= sharedPreferences.getString(KEY_USER,null)
            return GsonBuilder().create().fromJson(value, User::class.java)
        }
    private fun saveUserDetails(user: User?) {
        val userStr = GsonBuilder().create().toJson(user)
        editor.putString(KEY_USER,userStr)
        editor.apply()
    }

    fun setAuthenticationStatus(isAuthenticated: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_TOKEN, isAuthenticated).apply()
    }

    fun isAuthenticated(): Boolean {
        return sharedPreferences.getBoolean(KEY_TOKEN, false)
    }

    fun clearSession() {
        editor.remove("JWT")
        editor.apply()
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
    }
    fun isLoggedIn(): Boolean {
        return authToken != null
    }
    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("JWT", null)

    }
    fun fetchUserId(): String? {
        // Retrieve the user ID from SharedPreferences
        return sharedPreferences.getString("USER_ID", null)

    }

}
