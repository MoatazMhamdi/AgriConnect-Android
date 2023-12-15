package tn.sim5.agriconnect.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.models.DeleteUserRequest
import tn.sim5.agriconnect.utils.RetrofitImp

class UserViewModel : ViewModel() {
    private val apiService = RetrofitImp.apiService

    private val _deleteUserResult = MutableLiveData<Boolean>()
    val deleteUserResult: LiveData<Boolean> get() = _deleteUserResult

    fun deleteUserByNumTel(numTel: String) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteUserByNumTel(numTel)
                _deleteUserResult.value = response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                _deleteUserResult.value = false
            }
        }
    }
}