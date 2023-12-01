package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.databinding.ActivityFirstPageBinding
import tn.sim5.agriconnect.databinding.ActivityLoginBinding

class FirstPage : AppCompatActivity() {
    private lateinit var binding: ActivityFirstPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SignUpActivity", "Button Clicked")
        binding.btnFarmer.setOnClickListener {
            startActivity(Intent(this, login::class.java))

        }    }
}