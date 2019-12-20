package dev.mee42

import org.yaml.snakeyaml.Yaml

object Params {
    private lateinit var loadedFile: Map<String, Any>

    init {
        val text = getResources("debug: true\nprettyJson: true\nversion: 0.0.1\ndb: database.db\nbcryptIterations: 12", noDebug = true).toString(Charsets.UTF_8)
        val yaml = Yaml()
        yaml.load<Map<String,Any>>(text)
    }


    val debugMode = loadedFile["debug"] as Boolean
    val version = loadedFile["version"] as String
    val prettyJson = loadedFile["prettyJson"] as Boolean
    val databaseFile = loadedFile["db"] as String
    val bcryptIterations = loadedFile["bcryptIterations"] as Int
}