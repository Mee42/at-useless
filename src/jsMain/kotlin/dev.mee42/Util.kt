package dev.mee42

actual fun <T> T.toJson(): String {

    return "no"
}
actual inline fun <reified T> fromJson(str: String):T {
    return null as T
}
actual inline fun <reified T> String.fromJson():T {
    return null as T
}