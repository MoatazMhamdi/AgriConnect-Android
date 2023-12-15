package tn.sim5.agriconnect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tn.sim5.agriconnect.databinding.ActivityOtpBinding
import tn.sim5.agriconnect.ViewModels.VerifyOtpViewModel
import tn.sim5.agriconnect.databinding.ActivityOtpCBinding
import tn.sim5.agriconnect.ui.resetPasswordC

class otpC : AppCompatActivity() {
    private lateinit var binding: ActivityOtpCBinding
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, forgetPasswordC::class.java))
        }

        binding.btnClient.setOnClickListener {
            val numTel = binding.tiPhone.text.toString()
            val otp = binding.tiCode.text.toString()

            // Launch a coroutine to call the suspend function
            lifecycleScope.launch {
                verifyOtpViewModel.verifyOtp(numTel, otp)
            }
        }

        verifyOtpViewModel.otpResult.observe(this@otpC) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this@otpC, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()

                // Assuming you want to navigate to the reset activity on success
                startActivity(Intent(this@otpC, resetPasswordC::class.java))
            } else { Toast.makeText(this@otpC, "Error: OTP verification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
