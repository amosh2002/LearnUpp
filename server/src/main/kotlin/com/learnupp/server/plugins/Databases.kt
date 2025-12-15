package com.learnupp.server.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.learnupp.util.Logger
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

/**
 * DB configuration will be fully implemented later (Exposed schema, pgvector extension, migrations).
 * For now, we keep startup resilient: if no DB is available, the server still boots.
 */
fun Application.tryConfigureDatabases() {
    val jdbcUrl = System.getenv("JDBC_URL") ?: return
    val username = System.getenv("DB_USER") ?: "postgres"
    val password = System.getenv("DB_PASSWORD") ?: "password"

    try {
        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = (System.getenv("DB_POOL_SIZE") ?: "10").toInt()
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        Logger.i("Database", "Database connected: $jdbcUrl")
    } catch (t: Throwable) {
        Logger.w("Database", "Database not configured/available yet (continuing without DB): ${t.message}")
    }
}


