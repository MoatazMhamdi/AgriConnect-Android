package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.databinding.ActivityForgetPasswordBinding
import tn.sim5.agriconnect.databinding.ActivityLoginBinding

class ForgetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SignUpActivity", "Button Clicked")
        binding.btnfrgtpss.setOnClickListener {
            startActivity(Intent(this, OTP::class.java))

        }
           }
}