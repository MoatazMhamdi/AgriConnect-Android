package tn.sim5.agriconnect.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MyPrefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_IS_AUTHENTICATED = "is_authenticated"
    }

    fun setAuthenticationStatus(isAuthenticated: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_AUTHENTICATED, isAuthenticated).apply()
    }

    fun isAuthenticated(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_AUTHENTICATED, false)
    }

    fun clearSession() {
        sharedPreferences.edit().remove(KEY_IS_AUTHENTICATED).apply()
    }
    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("JWT", null)

    }
    fun fetchUserId(): String? {
        // Retrieve the user ID from SharedPreferences
        return sharedPreferences.getString("USER_ID", null)

    }

}
