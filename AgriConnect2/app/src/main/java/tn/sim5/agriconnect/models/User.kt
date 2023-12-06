package tn.sim5.agriconnect.models

data class FarmerSignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val numTel: String
)

data class FarmerSignUpResponse(
    val message: String
)

data class LoginRequest(
    val numTel: String,
    val password: String
)

data class LoginResponse(
    val userId: String,
    val message: String,
    val token: String // Add this property for the JWT token
)