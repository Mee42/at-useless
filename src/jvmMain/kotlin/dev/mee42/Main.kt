package dev.mee42

import io.ktor.application.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.security.SecureRandom
import java.util.*


fun main() {
    embeddedServer(Netty, port = PORT, host = HOST) {
        routing {
            addResourceFile("/","index.html","text/html; charset=utf-8")
            addResourceFile("/home","home.html","text/html; charset=UTF-8")

            addResourceFile("/index.css", "index.css", "text/css; charset=utf-8")
            addResourceFile("/home.css", "home.css", "text/css; charset=utf-8")

            static("/static") {
                resource("chat.mee42.js")
            }
            api()
        }
    }.start(wait = true)

}

private fun Routing.api() {
    route("/api") {
        get("/version") {
            call.respondText(contentType = ContentType.parse("application/json")) {
                JsonObjects.VersionInformation(version = Params.version).toJson()
            }
        }
        route("/v1") {
            v1api()
        }
    }
}

private val tempAuthList = mapOf(
    "bob" to "hunter2",
    "joe" to "password",
    "arson" to "arson"
)

// token -> username
class CachedToken(val token: String, val username: String, val expires: Long)
private val tokenCache = mutableListOf<CachedToken>()



private fun Route.v1api() {
    route("/auth") {
        post("/signin") {
            val auth = call.receiveText().fromJson<JsonObjects.SignInObject>()
            if(!tempAuthList.containsKey(auth.username)){
                call.respond(HttpStatusCode.Forbidden,JsonObjects.SignInErrorResponse(
                    message = "User with given username does not exist",
                    select = "username"
                ))
            }
            if(auth.username != "no" && tempAuthList[auth.username] != auth.password){
                call.respond(HttpStatusCode.Forbidden, JsonObjects.SignInErrorResponse(
                    message = "Password is incorrect",
                    select = "password"
                ))
            }
            val token = generateRandomToken()
            tokenCache.add(CachedToken(token,auth.username,Long.MAX_VALUE))
            call.respond(HttpStatusCode.OK,JsonObjects.SignInResponse(
                token = token
            ))
        }
    }
}

fun generateRandomToken(): String {
    val bytes = ByteArray(64) { 0x0 }
    SecureRandom.getInstanceStrong().nextBytes(bytes)
    return Base64.getEncoder().encodeToString(bytes)
}

fun Routing.addResourceFile(path: String, filename: String, contentType: String) {
    get(path) { call.respondBytes(contentType = ContentType.parse(contentType), bytes = getResources(filename)) }
}
