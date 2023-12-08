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

class reset : AppCompatActivity() {
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()

    private lateinit var binding: ActivityResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReset.setOnClickListener {
            val numTel = binding.tiPhone.text.toString()
            val newPassword = binding.tiNewPassword.text.toString()

            lifecycleScope.launch {
                resetPasswordViewModel.resetPassword(numTel, newPassword)
            }
        }

        resetPasswordViewModel.passwordReset.observe(this@reset) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(
                    this@reset,
                    "Password Reset Successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the login screen or another appropriate screen
                startActivity(Intent(this@reset, login::class.java))
            } else {
                Toast.makeText(
                    this@reset,
                    "Error: Password reset failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}