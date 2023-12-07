package tn.sim5.agriconnect.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.sim5.agriconnect.Repository.UserRepository
import tn.sim5.agriconnect.models.EditProfileResponse
import tn.sim5.agriconnect.models.Result // Make sure to import the correct Result class

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _editProfileResult = MutableLiveData<Result<EditProfileResponse>>()
    val editProfileResult: LiveData<Result<EditProfileResponse>> get() = _editProfileResult

    fun editProfile(token: String, userId: String, fieldsToUpdate: Map<String, String>) {
        _editProfileResult.value = Result.Loading

        userRepository.editProfile(token, userId, fieldsToUpdate).observeForever {
            _editProfileResult.value = it
        }
    }
}
