package dev.mee42

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import org.jdbi.v3.core.kotlin.mapTo
import java.time.Instant
import java.util.*


fun Route.authApi() {
    route("/auth") {
        post("/signin") {
            signIn()
        }
        post("/register") {
            register()
        }
    }
}

suspend fun PipelineContext<Unit,ApplicationCall>.signIn(){
    class UserIDTokenCombo(val passwordHash: String, val userID: Int)
    val auth = call.receive<JsonObjects.SignInRequest>()
    val hashList = Database.withHandle {
        createQuery("SELECT passwordHash,userID FROM users WHERE username = :username")
            .bind("username",auth.username)
            .mapTo<UserIDTokenCombo>()
            .list()
    }
    if(hashList.isEmpty()){
        return call.respond(
            HttpStatusCode.Unauthorized, JsonObjects.SignInErrorResponse(
                message = "User with given username does not exist",
                select = "username"
            ))
    }
    if(hashList.none { BCrypt.verifyer().verify(auth.password.toCharArray(),it.passwordHash).verified }){
        return call.respond(
            HttpStatusCode.Forbidden, JsonObjects.SignInErrorResponse(
                message = "Password is invalid",
                select = "password"
            ))
    }
    val id = hashList
        .first { BCrypt.verifyer().verify(auth.password.toCharArray(),it.passwordHash).verified }
    // they are authorized
    val token = generateAndInsertToken(id.userID)
    call.respond(JsonObjects.SignInResponse(token = token))
}

fun generateAndInsertToken(userID: Int): String {
    val token = generateRandomToken()
    Database.useHandle {
        createUpdate("INSERT INTO tokens (token, created,userID) VALUES (:token,:created,:userID)")
            .bind("token",token)
            .bind("created", Instant.now().epochSecond)
            .bind("userID", userID)
            .execute()
    }
    return token
}


suspend fun PipelineContext<Unit,ApplicationCall>.register(){
    val request = call.receive<JsonObjects.RegisterRequest>()
    // check each of them, one-by-one
    // starting with discord username
    val validateDiscord = validateDiscordUsername(request.discordUsername)
    if(validateDiscord.isPresent){
        return call.respond(
            HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
                message = validateDiscord.get(),
                select = "discord"
            ))
    }
    val validateUsername = initialValidateUsername(request.username)
    if(validateUsername.isPresent){
        return call.respond(HttpStatusCode.BadRequest,JsonObjects.RequestErrorResponse(
            message = validateUsername.get(),
            select = "username"
        ))
    }
    if(request.password.isBlank()) {
        return call.respond(HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
            message = "password can not be blank",
            select = "password"
        ))
    }
    if(request.password.length > 128) {
        return call.respond(HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
            message = "the max password length is 128",
            select = "password"
        ))
    }
    // TODO ratelimit this endpoint heavily

    // now check all the things that require a database hit
    val yes = Database.withHandle {
        createQuery("""SELECT userID FROM users WHERE username = :username""")
            .bind("username",request.username)
            .mapTo<String>()
            .findFirst()
    }
    if(yes.isPresent) {
        return call.respond(HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
            message = "username is already taken :(",
            select  = "username"
        ))
    }
    // we all good?
    // we all good
    val userID = Database.withHandle {
        createUpdate("""
            INSERT INTO users (username,  passwordHash,   discordUsername, discordTagIsVerified) 
                       VALUES (:username, :passwordHash, :discordUsername, 0)""")
            .bind("username",request.username)
            .bind("passwordHash",BCrypt.withDefaults().hash(Params.bcryptIterations, request.password.toCharArray()))
            .bind("discordUsername", request.discordUsername)
            .executeAndReturnGeneratedKeys("userID")
            .mapTo<Int>()
            .first()
    }
    val token = generateAndInsertToken(userID)

    return call.respond(JsonObjects.RequestResponse(
        userID = userID,
        token = token))
}

private val VALID_CHARS = ('a'..'z').joinToString() +
                                 ('A'..'Z').joinToString() +
                                 ('0'..'9').joinToString() +
                                 "-_+/\\(){}[]'\""

fun initialValidateUsername(username: String): Optional<String> {
    username.forEach { char ->
        if(char !in VALID_CHARS){
            return Optional.of("username can not contain char '$char'")
        }
    }
    if(username.isEmpty()) return Optional.of("username can not be empty")
    if(username.isBlank()) return Optional.of("username can not be blank")
    if(username.length > 32) return Optional.of("username can not be longer then 32 characters")
    return Optional.empty()
}


fun validateDiscordUsername(discordUsername: String): Optional<String> {
    if(discordUsername.isBlank()) return Optional.of("discord username required")
    if(!discordUsername.contains("#")){
        return Optional.of("discord username must include discrim: the four numbers after the #")
    }
    if(discordUsername.count { it == '#' } > 1){
        return Optional.of("discord username can not include '#'")
    }
    val actualUsername = discordUsername.substring(0,discordUsername.indexOf('#'))
    val discrim = discordUsername.substring(discordUsername.indexOf('#') + 1)
    if(discrim.length != 4 || !discrim.all { it in ('0'..'9') }){
        return Optional.of("discord discrim must be a 4 digit number")
    }
    if(actualUsername == "discordtag" || actualUsername == "everyone" || actualUsername == "here") {
        return Optional.of("discord username can not be \"$actualUsername\"")
    }
    if(actualUsername.length < 2 || actualUsername.length > 32) {
        return Optional.of("discord username must be between 2 and 32 chars")
    }
    return Optional.empty()
}

