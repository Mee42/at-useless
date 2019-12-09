package dev.mee42


const val HTTPS = false
const val PORT = 8081
const val HOST = "127.0.0.1"
val SERVER_URL = (if (HTTPS) "https://" else "http://") + "$HOST:$PORT"