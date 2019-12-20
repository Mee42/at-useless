package dev.mee42

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jdbi.v3.core.kotlin.mapTo
import java.net.URI
import java.nio.file.*
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.stream.Stream

object MainKtYes

fun test(){
    val uri: URI = MainKtYes::class.java.getResource("/").toURI()
    val myPath: Path = if (uri.scheme == "jar") {
        val fileSystem: FileSystem = FileSystems.newFileSystem(uri, emptyMap<String, Any>())
        fileSystem.getPath("/resources")
    } else {
        Paths.get(uri)
    }
    val walk: Stream<Path> = Files.walk(myPath, 10, FileVisitOption.FOLLOW_LINKS)

    val it: Iterator<Path> = walk.iterator()
    while (it.hasNext()) {
        println(it.next())
    }

}

fun main() {
    test()
//    println(getResources("param.yaml", noDebug = true))
//    val yes = Object::getClass.javaClass.classLoader.pro(".")
    return
    embeddedServer(Netty, port = PORT, host = HOST) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            addResourceFile("/","<!DOCTYPE html>\n<html lang=\"us\">\n<head>\n    <title>@useless</title>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\" media=\"screen\" />\n</head>\n<body>\n\t<div id=\"titlebar\">\n\t\t<h1>@useless</h1>\n\t\t<h2>A chat app for useless people</h2>\n\t</div>\n\t<div id=\"content\">\n\t<div id=\"sidebar\">\n\t\t<div id=\"menu-bar\">\n\t\t\t<p>Top menu bar</p>\n\t\t</div>\n\t\t<div id=\"channel-list\">\n\t\t\t<ul>\n\t\t\t\t<li><p><i>#</i> Channel list bar 1</p></li>\n\t\t\t\t<li><p><i>#</i> Channel list bar 2</p></li>\n\t\t\t\t<li><p><i>#</i> Channel list bar 3</P></li>\n\t\t\t</ul>\n\t\t</div>\n\t\t<div id=\"bottom-menu-bar\"/>\n\t\t\t<p>Bottom menu bar</p>\n\t\t</div>\n\t\t\n\t</div>\n\t<div id=\"main\">\n\t\t<p>main</p>\n\t</div>\n\t</div>\n\n\n\n</body>\n</html>\n","text/html; charset=utf-8")
            addResourceFile("/home","<!DOCTYPE html>\n<html lang=\"us\">\n<head>\n    <title>chat@mee42</title>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"home.css\" media=\"screen\"/>\n    <script type=\"text/javascript\" src=\"static/useless.js\"></script>\n    </head>\n<body>\n<h1>\n    chat@mee42, a chat webapp that serves no real purpose\n</h1>\n\n<div id=\"boxes\">\n    <article id=\"signin\">\n        <h2>Sign In</h2>\n        <form id=\"signin-form\" onsubmit=\"return false\"><label>\n            <input class=\"input\" id=\"signin-username\" type=\"text\" placeholder=\"username\"/>\n            <input class=\"input\" id=\"signin-password\" type=\"text\" placeholder=\"password\"/>\n            <input type=\"submit\" value=\"submit\" onclick=\"window.kotlin.signInFormCall()\">\n            <span id=\"signin-error-text\">error</span>\n        </label></form>\n    </article>\n    <article>\n\t    <h2>Register</h2>\n\t    <form id=\"register-form\" onsubmit=\"return false\"><label>\n\t       <input type=\"input\" id=\"register-username\"  type=\"text\" placeholder=\"username\"/>\n\t       <input type=\"input\" id=\"register-password\"  type=\"text\" placeholder=\"password\"/>\n\t       <input type=\"input\" id=\"register-password2\" type=\"text\" placeholder=\"repeat password\"/>\n               <input type=\"input\" id=\"register-identifier\" type=\"text\" placeholder=\"identifing information\"/>\n               <input type=\"submit\" value=\"submit\" onclick=\"window.kotlin.registerFormCall()\"/>\n\t       <span id=\"register-error-text\">error</span>\n            </label></form>\t       \n    </article>\n</div>\n</body>\n</html>\n","text/html; charset=UTF-8")

            addResourceFile("\nbody {\n    margin: 0;\n}\n\nhtml {\n    background-color: black;\n}\n\n#titlebar {\n\tborder-bottom: 2px solid white;\n\tdisplay: flex;\n\tflex-direction: row;\n\talign-content: center;\t\n}\n\n#titlebar * {\n\tpadding-right: 30px;\n\tpadding-left:  30px;\n \talign-self: center;\n}\n\nbody {\n\theight: 100%;\n\tdisplay: flex;\n\tflex-direction: column;\n}\n\nhtml {\n\theight: 100%;\n}\n\n#content {\n\tdisplay: flex;\n\tflex-direction: row;\n\theight: 100%\n}\n\n#sidebar, #main {\n\tborder: white solid 1px;\n\theight: auto;\n\tmargin: 10px;\n}\n\n#sidebar {\n\tborder-right: 0px;\n\twidth: 17em;\n\tmargin-right: 0px;\n\tdisplay: flex;\n\tflex-direction: column;\n}\n\n\n#menu-bar, #bottom-menu-bar {\n\tbackground-color: red;\n}\n\n#channel-list {\n\tflex-grow: 88;\n}\n#channel-list {\n\talign-content:\n}\n#channel-list ul {\n\tlist-style-type: none;\n\tpadding-left: 10px;\n\tpadding-right: 10px;\n\tpadding-top: 0px;\n\tpadding-bottom: 0px;\n}\n\n\n\n\n#main {\n\tmargin-left: 0px;\n\twidth: auto;\n\tflex-grow: 2;\n}\n\nh1, h2, h3, h4, h5, p {\n\tfont-family: 'DejaVu Sans Mono', monospace;\n\tcolor: white;\n}\n\n", "\nbody {\n    margin: 0;\n}\n\nhtml {\n    background-color: black;\n}\n\n#titlebar {\n\tborder-bottom: 2px solid white;\n\tdisplay: flex;\n\tflex-direction: row;\n\talign-content: center;\t\n}\n\n#titlebar * {\n\tpadding-right: 30px;\n\tpadding-left:  30px;\n \talign-self: center;\n}\n\nbody {\n\theight: 100%;\n\tdisplay: flex;\n\tflex-direction: column;\n}\n\nhtml {\n\theight: 100%;\n}\n\n#content {\n\tdisplay: flex;\n\tflex-direction: row;\n\theight: 100%\n}\n\n#sidebar, #main {\n\tborder: white solid 1px;\n\theight: auto;\n\tmargin: 10px;\n}\n\n#sidebar {\n\tborder-right: 0px;\n\twidth: 17em;\n\tmargin-right: 0px;\n\tdisplay: flex;\n\tflex-direction: column;\n}\n\n\n#menu-bar, #bottom-menu-bar {\n\tbackground-color: red;\n}\n\n#channel-list {\n\tflex-grow: 88;\n}\n#channel-list {\n\talign-content:\n}\n#channel-list ul {\n\tlist-style-type: none;\n\tpadding-left: 10px;\n\tpadding-right: 10px;\n\tpadding-top: 0px;\n\tpadding-bottom: 0px;\n}\n\n\n\n\n#main {\n\tmargin-left: 0px;\n\twidth: auto;\n\tflex-grow: 2;\n}\n\nh1, h2, h3, h4, h5, p {\n\tfont-family: 'DejaVu Sans Mono', monospace;\n\tcolor: white;\n}\n\n", "text/css; charset=utf-8")
            addResourceFile("\n\nh1,h2 {\n    text-align: center;\n    font-family: 'DejaVu Sans Mono', monospace;\n    color: white;\n}\n\nbody {\n    display: flex;\n    flex-direction: column;\n    justify-content: center;\n    align-items: center;\n\n}\n\n#boxes {\n    flex-grow: 2;\n    width: 100%;\n   \n    display: flex;\n    flex-direction: row;\n    justify-content: space-evenly;\n    align-content: center;\n    align-items: center;\n}\n\narticle {\n    margin-bottom: auto;\n    /*margin-top: auto;*/\n    border: 2px solid white;\n    padding: 10px;\n    border-radius: 10px;\n    display: flex;\n    flex-direction: column;\n    align-content: center;\n    height: auto;\n}\narticle h2 {\n\ttext-align: center;\n}\narticle form label span {\n\ttext-align: center;\n}\narticle form label {\n    display: flex;\n    flex-direction: column;\n    padding: 3px;\n}\narticle form label input {\n    padding-left: 5px;\n    padding-right: 5px;\n    margin: 3px;\n    transition: background-color 100ms;\n    height: 2em;\n}\narticle form label input.errored.input {\n    border: 2px solid red;\n}\n\narticle form label span {\n    display: none;\n    color: red;\n}\narticle form label span.errored {\n    display: block;\n}\n\n\nhtml {\n    background-color: black;\n}\n", "\n\nh1,h2 {\n    text-align: center;\n    font-family: 'DejaVu Sans Mono', monospace;\n    color: white;\n}\n\nbody {\n    display: flex;\n    flex-direction: column;\n    justify-content: center;\n    align-items: center;\n\n}\n\n#boxes {\n    flex-grow: 2;\n    width: 100%;\n   \n    display: flex;\n    flex-direction: row;\n    justify-content: space-evenly;\n    align-content: center;\n    align-items: center;\n}\n\narticle {\n    margin-bottom: auto;\n    /*margin-top: auto;*/\n    border: 2px solid white;\n    padding: 10px;\n    border-radius: 10px;\n    display: flex;\n    flex-direction: column;\n    align-content: center;\n    height: auto;\n}\narticle h2 {\n\ttext-align: center;\n}\narticle form label span {\n\ttext-align: center;\n}\narticle form label {\n    display: flex;\n    flex-direction: column;\n    padding: 3px;\n}\narticle form label input {\n    padding-left: 5px;\n    padding-right: 5px;\n    margin: 3px;\n    transition: background-color 100ms;\n    height: 2em;\n}\narticle form label input.errored.input {\n    border: 2px solid red;\n}\n\narticle form label span {\n    display: none;\n    color: red;\n}\narticle form label span.errored {\n    display: block;\n}\n\n\nhtml {\n    background-color: black;\n}\n", "text/css; charset=utf-8")

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