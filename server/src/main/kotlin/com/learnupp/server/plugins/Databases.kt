package com.learnupp.server.plugins

import com.learnupp.server.db.DatabaseSeeder
import com.learnupp.util.Logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

/**
 * DB configuration will be fully implemented later (Exposed schema, pgvector extension, migrations).
 * For now, we keep startup resilient: if no DB is available, the server still boots.
 */
fun Application.tryConfigureDatabases(): Database? {
    val jdbcUrl = System.getenv("JDBC_URL") ?: "jdbc:postgresql://localhost:5432/learnupp"
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
        val db = Database.connect(dataSource)
        Logger.i("Database", "Database connected: $jdbcUrl")

        // Create tables and seed mock-like data for local dev
        DatabaseSeeder.initialize(db)
        DatabaseSeeder.seedIfEmpty()

        return db
    } catch (t: Throwable) {
        Logger.w("Database", "Database not configured/available yet (continuing without DB): ${t.message}")
        return null
    }
}


