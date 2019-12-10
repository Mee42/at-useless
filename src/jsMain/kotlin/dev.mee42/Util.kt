package dev.mee42

actual fun <T> T.toJson(): String {
    return JSON.stringify(this)
}
actual inline fun <reified T> fromJson(str: String):T {
    return JSON.parse(str)
}
actual inline fun <reified T> String.fromJson():T {
    return JSON.parse(this)
}