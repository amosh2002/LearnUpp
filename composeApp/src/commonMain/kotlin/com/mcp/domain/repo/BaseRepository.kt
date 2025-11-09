package com.mcp.domain.repo

interface BaseRepository {
    suspend fun preloadData() {
        if (needsRefresh()) {
            refreshData()
        }
    }

    suspend fun refreshData()
    suspend fun needsRefresh(): Boolean
}