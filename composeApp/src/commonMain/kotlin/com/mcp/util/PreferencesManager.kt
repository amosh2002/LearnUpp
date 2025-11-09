package com.mcp.util

interface PreferencesManager {
    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean?
    suspend fun remove(key: String)
    suspend fun clear()
}
