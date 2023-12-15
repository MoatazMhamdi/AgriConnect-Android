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
data class ClientSignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val numTel: String
)

data class ClientSignUpResponse(
    val message: String
)

data class LoginRequest(
    val numTel: String,
    val password: String
)

data class LoginResponse(
    val userId: String,
    val name: String, // Add this property for the user's name
    val email: String,
    val message: String,
    val token: String, // Add this property for the JWT token
)
data class EditProfileRequest(
    val userId: String?,
    val fieldsToUpdate: Map<String, String>
)

data class EditProfileResponse(
    val message: String,
    val updatedUser: User
)

data class User(
    val _id: String,
    val name: String,
    val lastName: String,
    val password: String,
    val numTel: String,

    // Add other properties as needed
)
data class ForgetPasswordRequest(val numTel: String)
data class VerifyOtpRequest(
    val numTel: String,
    val otp: String
)
data class OtpResponsee(val message: String)

data class OtpResponse(val otp: String)

data class ResetPasswordRequest(val numTel: String, val newPassword: String)
// ResetPasswordResponse.kt
data class ResetPasswordResponse(
    val message: String
)
data class DeleteUserResponse(
    val message: String,
)
data class DeleteUserRequest(
    val numTel: String
)
