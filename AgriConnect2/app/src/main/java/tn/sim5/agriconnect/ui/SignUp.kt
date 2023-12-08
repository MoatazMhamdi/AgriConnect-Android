package tn.sim5.agriconnect.ui
import FarmerSignUpViewModel
import android.content.Intent
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
         binding.btnReturn.setOnClickListener {
             startActivity(Intent(this, login::class.java))

            }
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
        when {
            name.isEmpty() || email.isEmpty() || numTel.isEmpty() || password.isEmpty() -> {
                // Handle case where some input is empty
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            numTel.length != 8 -> {
                // Handle case where the number of characters in numTel is not 8
                Toast.makeText(this, "Phone number should be 8 digits", Toast.LENGTH_SHORT).show()
            }
            !isValidEmail(email) -> {
                // Handle case where the email is not in the correct format
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            }
            password.length < 6 || !isValidPassword(password) -> {
                // Handle case where the password is weak
                Toast.makeText(this, "Weak password. It should be at least 6 characters and contain uppercase letters or numbers", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // All validations passed, proceed with sign-up
                viewModel.signUpFarmer(name, email, password, numTel)
                // You might want to show a loading indicator or something here

                // Check if numTel exists in the database
                viewModel.signUpResult.observe(this) { result ->
                    when {
                        result == "Sign up failed" -> {
                            // Handle other failure cases, e.g., invalid request
                            Toast.makeText(this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // User registered successfully
                            Toast.makeText(this, "Farmer registered successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, login::class.java))
                        }
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Add your email validation logic here, e.g., using a regular expression
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return email.matches(emailRegex)
    }

    private fun isValidPassword(password: String): Boolean {
        // Add your password validation logic here, e.g., checking for uppercase letters or numbers
        val containsUppercase = password.any { it.isUpperCase() }
        val containsNumber = password.any { it.isDigit() }
        return containsUppercase || containsNumber
    }





}
