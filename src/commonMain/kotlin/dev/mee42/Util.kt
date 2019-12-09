package dev.mee42

expect fun <T> T.toJson(): String
expect inline fun <reified T> fromJson(str: String):T
expect inline fun <reified T> String.fromJson():T