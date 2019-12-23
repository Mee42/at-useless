package dev.mee42

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.engine.js.JsClient
import io.ktor.client.request.get
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.*
import org.w3c.dom.Element
import kotlin.browser.*


fun homeEntry() {
    dynStorage.yes ="yes"
    dynStorage.signInFormCall = ::signInFormCall
}


//val signIn by lazyId("signin-form")
private val signInUsername by lazyId<Element>("signin-username") // TODO change to proper type
private val signInPassword by lazyId<Element>("signin-password")
private val signInErrorText by lazyId<Element>("signin-error-text")

@Suppress("unused")
fun signInFormCall() {
    val username = signInUsername.asDynamic().value.unsafeCast<String>()
    val password = signInPassword.asDynamic().value.unsafeCast<String>()
    println("username: \"$username\" password:\"$password\"")
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
//    GlobalScope.launch {
//        println("version:" + client.call("http://127.0.0.1:8081/api/version"){
//            method = HttpMethod.Get
//        }.response.call.receive<String>().fromJson<JsonObjects.VersionInformation>().version)
//    }
}