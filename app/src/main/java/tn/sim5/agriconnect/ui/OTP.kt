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

class OTP : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        binding.btnClient.setOnClickListener {
            val numTel = binding.tiPhone.text.toString()
            val otp = binding.tiCode.text.toString()

            // Launch a coroutine to call the suspend function
            lifecycleScope.launch {
                verifyOtpViewModel.verifyOtp(numTel, otp)
            }
        }

        verifyOtpViewModel.otpResult.observe(this@OTP) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this@OTP, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()

                // Assuming you want to navigate to the reset activity on success
                startActivity(Intent(this@OTP, reset::class.java))
            } else { Toast.makeText(this@OTP, "Error: OTP verification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
