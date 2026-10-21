package com.teyvat.hub.application.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.teyvat.hub.data.local.checklist.ChecklistDao
import com.teyvat.hub.data.mapper.toDomain
import com.teyvat.hub.data.mapper.toEntity
import com.teyvat.hub.domain.checklist.ChecklistItem

/**
 * Single source of truth for farming tasks.
 *
 * Pending state is derived from stored completion timestamps, never persisted,
 * so a device whose clock or timezone changes recovers on the next read.
 */
interface ChecklistRepository {
    fun observePending(): Flow<List<ChecklistItem>>
    fun observeAll(): Flow<List<ChecklistItem>>
    suspend fun markCompleted(id: String, at: Long)
    suspend fun addCustomTask(title: String)
    suspend fun delete(id: String)
}

@Singleton
class LocalChecklistRepository @Inject constructor(
    private val dao: ChecklistDao,
) : ChecklistRepository {

    override fun observePending(): Flow<List<ChecklistItem>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeAll(): Flow<List<ChecklistItem>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun markCompleted(id: String, at: Long) {
        val current = dao.findById(id) ?: return
        dao.update(current.copy(lastCompletedAt = at))
    }

    override suspend fun addCustomTask(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val item = ChecklistItem(
            id = CUSTOM_PREFIX + System.currentTimeMillis(),
            title = trimmed,
            cadence = com.teyvat.hub.domain.checklist.ResetCadence.NONE,
            isCustom = true,
        )
        dao.insert(item.toEntity())
    }

    override suspend fun delete(id: String) {
        val current = dao.findById(id) ?: return
        dao.delete(current)
    }

    private companion object {
        const val CUSTOM_PREFIX = "custom-"
    }
}
