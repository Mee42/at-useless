package dev.mee42

object JsonObjects {
    data class VersionInformation(val version: String)

    data class SignInRequest(val username: String, val password: String)
    data class SignInResponse(val token: String)
    data class SignInErrorResponse(val message: String, val select: String)

    data class MeResponse(val userID: Int,
                          val username: String,
                          val discordUsername: String,
                          val isVerified: Boolean)

    data class RegisterRequest(
        val username: String,
        val password: String,
        val discordUsername: String)
    data class RequestErrorResponse(val message: String, val select: String)
    data class RequestResponse(val userID: Int, val token: String)
}