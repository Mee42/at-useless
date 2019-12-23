package dev.mee42

import io.ktor.client.HttpClient
import org.w3c.dom.Element
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.hasClass


val dynStorage: dynamic
    get() = window.asDynamic().kotlin

val client = HttpClient()


inline fun dyn(init: dynamic.() -> Unit): dynamic {
    val obj = js("{}")
    init(obj)
    return obj
}


fun <T: Element> lazyId(id: String):Lazy<T> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (document.getElementById(id) ?: error("Can't find element $id")) as T
    }
}

val html by lazy { document.getElementsByTagName("html")[0] ?: error("Can't find <html>") }

fun main() {
    window.addEventListener("load", {
        println("Page loaded")
        window.asDynamic().kotlin = {}
        when {
            html.hasClass("index_html") -> {
                println("On index.html page!")
                indexEntry()
            }
            html.hasClass("home_html") -> {
                println("On home page!")
                homeEntry()
            }
            else -> {
                error("ohno, can't find entry to use")
            }
        }
    })
}