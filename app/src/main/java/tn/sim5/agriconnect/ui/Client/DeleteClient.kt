package tn.sim5.agriconnect.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tn.sim5.agriconnect.databinding.ActivityDeleteUserBinding
import tn.sim5.agriconnect.models.DeleteUserRequest
import tn.sim5.agriconnect.ViewModels.UserViewModel
import tn.sim5.agriconnect.databinding.ActivityDeleteClientBinding
import tn.sim5.agriconnect.databinding.ActivityEditCBinding

class DeleteClient : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteClientBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnReturn.setOnClickListener {

        }

        binding.btnrmv.setOnClickListener {
            removeUser()
        }
    }

    private fun removeUser() {
        val numTel = binding.tiPhone.text.toString()

        if (numTel.isEmpty()) {
            // Handle case where phone number is empty
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.deleteUserByNumTel(numTel)

        viewModel.deleteUserResult.observe(this) { success ->
            if (success) {
                // User deleted successfully
                Toast.makeText(this, "User removed successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, loginClient::class.java))
            } else {
                // User deletion failed
                Toast.makeText(this, "Failed to remove user. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
