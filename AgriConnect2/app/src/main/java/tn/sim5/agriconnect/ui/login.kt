package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.databinding.ActivityLoginBinding
import tn.sim5.agriconnect.databinding.ActivitySignUpBinding

class login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SignUpActivity", "Button Clicked")
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))

        }
    }
}