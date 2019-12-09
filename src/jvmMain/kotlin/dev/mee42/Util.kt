package dev.mee42

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

val internalGson : Gson = GsonBuilder()
    .applyIf(Params.prettyJson) { setPrettyPrinting() }
    .create()

actual fun <T> T.toJson(): String {
    return internalGson.toJson(this)
}
actual inline fun <reified T> fromJson(str: String):T {
    return internalGson.fromJson(str,T::class.java)
}
@JvmName("fromJson2")
actual inline fun <reified T> String.fromJson():T {
    return internalGson.fromJson(this,T::class.java)
}




fun <T> T.applyIf(conditional: Boolean, block: T.() -> Unit):T{
    if(conditional) apply(block)
    return this
}

fun getResources(name: String, noDebug: Boolean = false): ByteArray =
    if(!noDebug && Params.debugMode) File("src/jvmMain/resources/$name").readBytes() else
        Object::getClass.javaClass.classLoader.getResourceAsStream(name)?.readAllBytes() ?: error("Can't read $name")