package dev.mee42


private val VALID_CHARS = ('a'..'z').joinToString() +
        ('A'..'Z').joinToString() +
        ('0'..'9').joinToString() +
        "-_+/\\(){}[]'\""


fun constantValidateUsername(username: String): ErrorOrSuccess<String> {
    username.forEach { char ->
        if(char !in VALID_CHARS){
            return "username can not contain char '$char'".asError()
        }
    }
    if(username.isEmpty()) return "username can not be empty".asError()
    if(username.isBlank()) return "username can not be blank".asError()
    if(username.length > 32) return "username can not be longer then 32 characters".asError()
    return ErrorOrSuccess.success()
}


fun constantValidatePassword(password: String): ErrorOrSuccess<String> {
    if(password.isBlank()) return "password can not be blank".asError()
    if(password.length > 128) return "max password length is 128 characters".asError()
    return ErrorOrSuccess.success()
}

fun constantValidateInfo(info: String): ErrorOrSuccess<String> {
    if(info.length > 128) return "max info length is 128 characters".asError()
    if()
}

//    kept in case we ever need it
/*fun constantValidateDiscordUsername(discordUsername: String): ErrorOrSuccess<String> {
    if(discordUsername.isBlank()) return "discord username required".asError()
    if(!discordUsername.contains("#")){
        return "discord username must include discrim: the four numbers after the #".asError()
    }
    if(discordUsername.count { it == '#' } > 1) return "discord username can not include '#'".asError()

    val actualUsername = discordUsername.substring(0,discordUsername.indexOf('#'))
    val discrim = discordUsername.substring(discordUsername.indexOf('#') + 1)
    if(discrim.length != 4 || !discrim.all { it in ('0'..'9') }){
        return "discord discrim must be a 4 digit number".asError()
    }
    if(actualUsername == "discordtag" || actualUsername == "everyone" || actualUsername == "here") {
        return "discord username can not be \"$actualUsername\"".asError()
    }
    if(actualUsername.length < 2 || actualUsername.length > 32) {
        return "discord username must be between 2 and 32 chars".asError()
    }
    return ErrorOrSuccess.success()
}*/
