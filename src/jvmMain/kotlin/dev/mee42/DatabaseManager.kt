package dev.mee42

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.io.File

object Database {
    private val jdbi = createJdbi()

    init {
        if(!File(Params.databaseFile).exists()){
            // build up the database
            useHandle {
                execute("""
                    DROP TABLE users;
                    DROP TABLE tokens;
                    CREATE TABLE users(
                        userID INTEGER AUTOINCREMENT PRIMARY KEY NOT NULL,
                        username VARCHAR(32) NOT NULL,
                        passwordHash VARCHAR(128) NOT NULL,
                        discordUsername VARCHAR(128) NULL,
                        discordTagIsVerified INTEGER
                    );
                    CREATE TABLE tokens (
                        token VARCHAR(128) NOT NULL PRIMARY KEY,
                        created INTEGER NOT NULL,
                        userID INTEGER NOT NULL,
                        FOREIGN KEY (userID) REFERENCES users(userID)
                    )
                """.trimIndent())
            }
        }
    }

    private fun createJdbi(): Jdbi {
        return Jdbi.create("jdbc:sqlite:${Params.databaseFile}")
            .installPlugins()
    }

    fun useHandle(handle: Handle.() -> Unit) {
        jdbi.useHandleUnchecked { handle(it) }
    }

    fun <R> withHandle(handle: Handle.() -> R):R {
        return jdbi.withHandleUnchecked { handle(it) }
    }
}
