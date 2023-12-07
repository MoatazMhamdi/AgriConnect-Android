package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.ViewModels.FarmerLoginViewModel
import tn.sim5.agriconnect.databinding.ActivityLoginBinding

class login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: FarmerLoginViewModel
    private var numTel = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FarmerLoginViewModel::class.java)
        viewModel.init(this)

//        Log.d("SignUpActivity", "Button Clicked")
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))

        }
        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))

        }
        binding.btnLogin.setOnClickListener(){
            validateAndLogin()

        }
    }
    private fun validateAndLogin() {
        numTel = binding.tiPhone.text.toString()
        password = binding.tiPassword.text.toString()

        if (numTel.isNotEmpty() && password.isNotEmpty()) {
            viewModel.loginFarmer(numTel, password)

            startActivity(Intent(this, Profile::class.java))


        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

}