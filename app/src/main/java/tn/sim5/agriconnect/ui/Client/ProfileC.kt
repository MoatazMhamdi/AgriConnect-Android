package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.ViewModels.FarmerLoginViewModel
import tn.sim5.agriconnect.databinding.ActivityProfileBinding
import tn.sim5.agriconnect.databinding.ActivityProfileCBinding
import tn.sim5.agriconnect.models.LoginResponse
import tn.sim5.agriconnect.ui.login
import tn.sim5.agriconnect.utils.SessionManager

class ProfileC : AppCompatActivity() {
    private lateinit var binding: ActivityProfileCBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: FarmerLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SessionManager
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(FarmerLoginViewModel::class.java)
        //observe name, email
        // Retrieve user information from Intent extras
        viewModel.userFullName.observe(this, Observer { fullName ->
            binding.txtFullName.text = fullName
        })

        viewModel.userEmail.observe(this, Observer { email ->
            binding.txtEmail.text = email
        })
        // Check if the user is authenticated
        println("Is authenticated when checking: ${sessionManager.isAuthenticated()}")

        if (sessionManager.isAuthenticated()) {
            // User is authenticated, show the logout button
            binding.btnLogout.visibility = View.VISIBLE

        } else {
            // User is not authenticated, hide the logout button
            binding.btnLogout.visibility = View.GONE
        }

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }
        binding.btnremoveaccount.setOnClickListener {
            startActivity(Intent(this, DeleteUser::class.java))
        }

        binding.btnLogout.setOnClickListener {
            // Clear session on logout
            sessionManager.clearSession()

            // Debug logs
            println("Is authenticated after logout: ${sessionManager.isAuthenticated()}")

            // Check if the session is cleared successfully
            if (!sessionManager.isAuthenticated()) {
                // Session cleared successfully, display a message or log a statement
                println("Session cleared successfully")
                Toast.makeText(this, "User Logged Out Succefuly", Toast.LENGTH_SHORT).show()
                // Redirect to the login page
                startActivity(Intent(this, login::class.java))
                finish() // Close the current activity to prevent going back to the profile screen using the back button
            } else {
                // Session not cleared, display an error message or log an error statement
                println("Error clearing session")
            }
        }
        val numTel = intent.getStringExtra("USER_NUM_TEL")
        if (numTel != null) {
            binding.txtFullName.text = numTel
        }
    }
}