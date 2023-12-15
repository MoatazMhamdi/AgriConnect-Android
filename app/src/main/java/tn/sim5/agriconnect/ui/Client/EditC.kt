package tn.sim5.agriconnect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.ViewModels.ProfileViewModel
import tn.sim5.agriconnect.databinding.ActivityEditCBinding
import tn.sim5.agriconnect.databinding.ActivityEditProfileBinding
import tn.sim5.agriconnect.databinding.ActivityProfileCBinding
import tn.sim5.agriconnect.models.EditProfileResponse
import tn.sim5.agriconnect.utils.SessionManager

class EditC : AppCompatActivity() {
    private lateinit var binding: ActivityEditCBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Retrieve token and userId from the session
        val token = sessionManager.fetchAuthToken()
        val userId = sessionManager.fetchUserId()

        // Check if token and userId are available

        // Handle button click to return to Profile activity
        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, ProfileC::class.java))
        }
        if (token != null && userId != null) {
            // Handle button click to edit profile
            binding.btnEdit.setOnClickListener {
                // Assuming you have fieldsToUpdate as a map of fields to update
                val fieldsToUpdate = mapOf(
                    "name" to binding.tiFullName.text.toString(),
                    "email" to binding.tiEmail.text.toString(),
                    "numTel" to binding.tiPhone.text.toString(),
                    "password" to binding.tiPassword.text.toString(),

                    // Add other fields as needed
                )

                // Call editProfile function with the retrieved token, userId, and fieldsToUpdate
                viewModel.editProfile(token, userId, fieldsToUpdate)
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}