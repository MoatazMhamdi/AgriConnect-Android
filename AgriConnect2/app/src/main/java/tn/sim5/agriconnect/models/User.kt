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
