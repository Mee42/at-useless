package dev.mee42

import org.yaml.snakeyaml.Yaml

object Params {
    private val loadedFile = {
        val text = getResources("param.yaml", noDebug = true).toString(Charsets.UTF_8)
        val yaml = Yaml()
        yaml.load<Map<String,Any>>(text)
    }()
    val debugMode = loadedFile["debug"] as Boolean
    val version = loadedFile["version"] as String
    val prettyJson = loadedFile["prettyJson"] as Boolean
}