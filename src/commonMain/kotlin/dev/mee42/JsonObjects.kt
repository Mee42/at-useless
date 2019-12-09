package dev.mee42

object JsonObjects {
    open class Sendable {
        override fun toString(): String {
            return this.toJson()
        }
    }

    data class VersionInformation(val version: String): Sendable()

    data class SignInObject(val username: String, val password: String): Sendable()
    data class SignInResponse(val token: String): Sendable()
    data class SignInErrorResponse(val message: String, val select: String): Sendable()
}