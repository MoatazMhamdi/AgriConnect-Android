package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.ViewModels.ResetPasswordViewModel
import tn.sim5.agriconnect.databinding.ActivityProfileBinding
import tn.sim5.agriconnect.databinding.ActivityResetBinding
import tn.sim5.agriconnect.databinding.ActivityResetPasswordCBinding

class resetPasswordC : AppCompatActivity() {
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()

    private lateinit var binding: ActivityResetPasswordCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordCBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReset.setOnClickListener {
            val numTel = binding.tiPhone.text.toString()
            val newPassword = binding.tiNewPassword.text.toString()

            lifecycleScope.launch {
                resetPasswordViewModel.resetPassword(numTel, newPassword)
            }
        }

        resetPasswordViewModel.passwordReset.observe(this@resetPasswordC) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(
                    this@resetPasswordC,
                    "Password Reset Successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the login screen or another appropriate screen
                startActivity(Intent(this@resetPasswordC, loginClient::class.java))
            } else {
                Toast.makeText(
                    this@resetPasswordC,
                    "Error: Password reset failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}