package tn.sim5.agriconnect.ui
import FarmerSignUpViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.databinding.ActivitySignUpBinding


class SignUp : AppCompatActivity() {

    private lateinit var viewModel: FarmerSignUpViewModel
    private lateinit var binding: ActivitySignUpBinding
    private var name = ""
    private var email = ""
    private var numTel = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SignUpActivity", "Button Clicked")

        viewModel = ViewModelProvider(this).get(FarmerSignUpViewModel::class.java)

        binding.btnSignUp.setOnClickListener {
            validateAndSignUp()
        }
    }
    private fun validateAndSignUp() {
        val name = binding.tiFullName.text.toString()
        val email = binding.tiEmail.text.toString()
        val numTel = binding.tiPhone.text.toString()
        val password = binding.tiPassword.text.toString()

        // Validate input
        if (name.isNotEmpty() && email.isNotEmpty() && numTel.isNotEmpty() && password.isNotEmpty()) {
            // Trigger API call
            viewModel.signUpFarmer(name, email, password, numTel)
        } else {
            // Handle case where some input is empty
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }


}
