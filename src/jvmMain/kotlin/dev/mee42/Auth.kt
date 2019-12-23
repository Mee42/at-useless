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

// TODO ratelimit this endpoint heavily based on a both an endpoint bucket
//      and a per-ip bucket as well
suspend fun PipelineContext<Unit,ApplicationCall>.register(){
    val request = call.receive<JsonObjects.RegisterRequest>()


    constantValidateUsername(request.username).onError {
        return call.respond(HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
            message = it,
            select = "username"
        ))
    }
    constantValidatePassword(request.password).onError {
        return call.respond(HttpStatusCode.BadRequest, JsonObjects.RequestErrorResponse(
            message = it,
            select = "password"
        ))
    }
    constantValidateInfo(request.info).onError {
        return call.respond(HttpStatusCode.BadRequest,JsonObjects.RequestErrorResponse(
            message =it,
            select = "info"
        ))
    }

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


