package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.databinding.ActivityEditProfileBinding
import tn.sim5.agriconnect.databinding.ActivityForgetPasswordBinding
import tn.sim5.agriconnect.databinding.ActivityOtpBinding

class OTP : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }
}