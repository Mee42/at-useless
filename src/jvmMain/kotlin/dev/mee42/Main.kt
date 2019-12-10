package dev.mee42

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jdbi.v3.core.kotlin.mapTo
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.*


fun main() {
    embeddedServer(Netty, port = PORT, host = HOST) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            addResourceFile("/","index.html","text/html; charset=utf-8")
            addResourceFile("/home","home.html","text/html; charset=UTF-8")

            addResourceFile("/index.css", "index.css", "text/css; charset=utf-8")
            addResourceFile("/home.css", "home.css", "text/css; charset=utf-8")

            static("/static") {
                resource("useless.js")
            }
            api()
        }
    }.start(wait = true)

}


private fun Routing.api() {
    route("/api") {
        get("/version") {
            call.respond(JsonObjects.VersionInformation(version = Params.version))
        }
        post("/ping") {
            call.respondText(call.receiveText())
        }
        authApi()
        get("/me") {
            val authHeader = call.checkTokenHeader() ?: return@get
            val (username,discord) = Database.withHandle {
                createQuery("""SELECT username, discordUsername, discordTagIsVerified FROM users WHERE userID = :userID""")
                    .bind("userID",authHeader.userID)
                    .map { row -> row.getColumn("username",String::class.java) to
                            (row.getColumn("discordUsername",String::class.java) to
                            (row.getColumn("discordTagIsVerified",Integer::class.java).toInt() == 1))}
                    .first()
            }
            val (discordUsername, isVerified) = discord
            call.respond(JsonObjects.MeResponse(
                userID = authHeader.userID,
                username = username,
                discordUsername = discordUsername,
                isVerified = isVerified))
        }
    }
}






enum class TokenFailureState(val code: HttpStatusCode){
    NOT_PRESENT(HttpStatusCode.Unauthorized),
    MALFORMED(HttpStatusCode.BadRequest),
    INVALID_OR_EXPIRED(HttpStatusCode.Forbidden)
}

data class AuthorizedUserInfo(val tokenUsed: String, val created :Long, val userID: Int)

suspend fun ApplicationCall.checkTokenHeader(): AuthorizedUserInfo? {
    return validTokenHeader().getFromEither({ a -> a },{
        respond(it.code,"")
        null
    })
}

fun ApplicationCall.validTokenHeader(): Either<AuthorizedUserInfo,TokenFailureState> {
    val token = tokenHeader ?: return Either.ofB(TokenFailureState.NOT_PRESENT)
    if(!token.matches(Regex("""^[a-zA-Z0-9/+]{44}${'$'}"""))){
        return Either.ofB(TokenFailureState.MALFORMED)
    }
    // now we do the database hit

    class TokenTableEntry(val token: String, val created: Long, val userID: Int)
    val result = Database.withHandle {
        createQuery("""SELECT token, created, userID FROM tokens WHERE token = :token""")
            .bind("token",token)
            .mapTo<TokenTableEntry>()
            .findFirst()
    }
    if(result.isEmpty) {
        return Either.ofB(TokenFailureState.INVALID_OR_EXPIRED)
    }
    val authorizedUser = result.get()
    if(Duration.between(Instant.now(),Instant.ofEpochMilli(authorizedUser.created)) > Duration.ofDays(1)){
        // mark as expired
        Database.useHandle {
            createUpdate("""DELETE FROM tokens WHERE token = :token""")
                .bind("token",token)
                .execute()
        }
        return Either.ofB(TokenFailureState.INVALID_OR_EXPIRED)
    }
    return Either.ofA(AuthorizedUserInfo(token,authorizedUser.created,authorizedUser.userID))
}


private val ApplicationCall.tokenHeader: String?
    get() = this.request.headers["token"]

fun generateRandomToken(): String {
    val bytes = ByteArray(33) { 0x0 }
    SecureRandom.getInstanceStrong().nextBytes(bytes)
    return Base64.getEncoder().encodeToString(bytes)
}

fun Routing.addResourceFile(path: String, filename: String, contentType: String) {
    get(path) { call.respondBytes(contentType = ContentType.parse(contentType), bytes = getResources(filename)) }
}
