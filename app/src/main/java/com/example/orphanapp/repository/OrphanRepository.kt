package com.example.orphanapp.repository

import com.example.orphanapp.model.Orphan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface OrphanRepository {
    fun getOrphans(): Flow<List<Orphan>>
    suspend fun addOrphan(orphan: Orphan)
    suspend fun updateOrphan(orphan: Orphan)
}

class OrphanRepositoryImpl : OrphanRepository {
    private val _orphans = MutableStateFlow<List<Orphan>>(emptyList())

    override fun getOrphans(): Flow<List<Orphan>> = _orphans.asStateFlow()

    override suspend fun addOrphan(orphan: Orphan) {
        _orphans.value = _orphans.value + orphan
    }

    override suspend fun updateOrphan(orphan: Orphan) {
        val currentList = _orphans.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == orphan.id }
        if (index != -1) {
            currentList[index] = orphan
            _orphans.value = currentList
        }
    }
}
