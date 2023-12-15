package tn.sim5.agriconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.ViewModels.FarmerLoginViewModel
import tn.sim5.agriconnect.databinding.ActivityLoginBinding
import tn.sim5.agriconnect.databinding.ActivityLoginClientBinding
import tn.sim5.agriconnect.ui.ProfileC

class loginClient : AppCompatActivity() {
    private lateinit var binding: ActivityLoginClientBinding
    private lateinit var viewModel: FarmerLoginViewModel
    private var numTel = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FarmerLoginViewModel::class.java)
        viewModel.init(this)

//        Log.d("SignUpActivity", "Button Clicked")
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, Register_client::class.java))

        }
        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, forgetPasswordC::class.java))

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

      //      startActivity(Intent(this, ProfileC::class.java))
            viewModel.loginResult.observe(this) { result ->
                when (result) {
                    "Login failed" -> {
                        Toast.makeText(this, "Phone number or Password are incorrect!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileC::class.java))
                    }
                }
            }
        /*    viewModel.loginResult.observe(this) { result ->
                if (result == "Success") {
                    // User logged in successfully, get user information and pass it to the profile activity
                    val userFullName = viewModel.getUserFullName()
                    val userEmail = viewModel.getUserEmail()

                    // Pass user information to the profile activity
                    val intent = Intent(this, ProfileC::class.java)
                    intent.putExtra("USER_FULL_NAME", userFullName)
                    intent.putExtra("USER_EMAIL", userEmail)
                    startActivity(intent)
                } else {
                    // Show a Toast message based on the result
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                }
            }
*/

        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

}