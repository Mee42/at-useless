package dev.mee42

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.engine.js.JsClient
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.*
import org.w3c.dom.Element
import kotlin.browser.*


fun lazyId(id: String):Lazy<Element> {
    return lazy { document.getElementById(id) ?: error("Can't find element $id") }
}

val dynStorage: dynamic
    get() = window.asDynamic().kotlin

fun main() {
    window.asDynamic().kotlin = {}
    println("home!")
    dynStorage.yes ="yes"
    dynStorage.signInFormCall = { signInFormCall() }
}

val client = HttpClient()

//val signIn by lazyId("signin-form")
val signInUsername by lazyId("signin-username")
val signInPassword by lazyId("signin-password")
val signInErrorText by lazyId("signin-error-text")
@Suppress("unused")
fun signInFormCall() {
    val username = signInUsername.asDynamic().value.unsafeCast<String>()
    val password = signInPassword.asDynamic().value.unsafeCast<String>()
    println("username: \"$username\" password:\"$password\"")
    val result = client.call("$SERVER_URL/api/version") {
        method = HttpMethod.Post
        body = "body"
    }
    console.log("got: $result")
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