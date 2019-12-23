package dev.mee42

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSpanElement
import kotlin.dom.addClass


fun homeEntry() {
    dynStorage.yes ="yes"
    dynStorage.signInFormCall = ::signInFormCall
}


private val signInUsername by lazyId<HTMLInputElement>("signin-username") // TODO change to proper type
private val signInPassword by lazyId<HTMLInputElement>("signin-password")
private val signInErrorText by lazyId<HTMLSpanElement>("signin-error-text")

@Suppress("unused")
fun signInFormCall() {
    val username = signInUsername.value
    val password = signInPassword.value
    println("username: \"$username\" password:\"$password\"")
    // check the username
    fun errorWith(select: String, message: String) {
        println("error with $select: $message")
        val selected = when(select) {
            "username" -> signInUsername
            "password" -> signInPassword
            else -> error("Don't know select $select")
        }
        selected.classList.add("errored")
        selected.select()
        signInErrorText.textContent = message
        signInErrorText.classList.add("errored")
    }

    constantValidateUsername(username).onError {
        errorWith("username", it)
    }

//    val result = client.call("$SERVER_URL/api/version") {
//        method = HttpMethod.Post
//        body = "body"
//    }
//    console.log("got: $result")
//            if (result.response.status == HttpStatusCode.Forbidden) {
//                val error = result.response.call
//
//                console.log("got $error")
//                val select = if (error.select == "username") signInUsername else signInPassword
//                select.classList.add("errored")
//                signInErrorText.textContent = error.message
//                signInErrorText.classList.add("errored")
//            }

}