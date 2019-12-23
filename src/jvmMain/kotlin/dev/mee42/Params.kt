package dev.mee42

import org.yaml.snakeyaml.Yaml

object Params {
    private val loadedFile: Map<String, Any> = {
        val text = getResources("param.yaml", noDebug = true).toString(Charsets.UTF_8)
        val yaml = Yaml()
        yaml.load<Map<String, Any>>(text)
    }()

    val debugMode: Boolean
        get() = loadedFile["debug"] as Boolean

    val version = loadedFile["version"] as String
    val prettyJson = loadedFile["prettyJson"] as Boolean
    val databaseFile = loadedFile["db"] as String
    val bcryptIterations = loadedFile["bcryptIterations"] as Int
}
