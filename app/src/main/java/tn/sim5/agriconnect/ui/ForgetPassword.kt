package tn.sim5.agriconnect.ui

import ForgetPasswordViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.databinding.ActivityForgetPasswordBinding
import tn.sim5.agriconnect.models.Result

class ForgetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this@ForgetPassword, login::class.java))

        }
        binding.btnfrgtpss.setOnClickListener {
            val numTel = binding.tiPhone.text.toString()

            // Launch a coroutine to call the suspend function
            lifecycleScope.launch {
                forgetPasswordViewModel.forgetPassword(numTel)
            }
        }

        forgetPasswordViewModel.otpResult.observe(this@ForgetPassword) { otp ->
            Toast.makeText(this@ForgetPassword, "OTP: $otp", Toast.LENGTH_SHORT).show()
            // Navigate to the next screen (e.g., OTP screen)
            startActivity(Intent(this@ForgetPassword, OTP::class.java))
        }
    }
}
