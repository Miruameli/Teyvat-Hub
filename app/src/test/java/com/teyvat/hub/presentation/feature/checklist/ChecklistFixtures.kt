package com.teyvat.hub.presentation.feature.checklist

import kotlinx.coroutines.flow.flow
import com.teyvat.hub.application.repository.ChecklistRepository
import com.teyvat.hub.domain.checklist.ChecklistItem
import com.teyvat.hub.domain.checklist.ResetCadence

/**
 * Shared fixtures for the checklist tests.
 *
 * The fakes live here so the test class stays focused on the behaviour it
 * covers and stays within the file size limit.
 */

/** Builds a checklist item with a fixed identity and cadence. */
internal fun checklistItem(
    id: String,
    cadence: ResetCadence,
    completedAt: Long? = null,
    isCustom: Boolean = false,
) = ChecklistItem(
    id = id,
    title = id,
    cadence = cadence,
    lastCompletedAt = completedAt,
    isCustom = isCustom,
)

/**
 * Repository double that serves a fixed list and reports added titles.
 *
 * [onAdd] receives the title exactly as the view model passed it, so a test
 * can assert what the trimming rule received.
 */
internal fun fakeChecklistRepository(
    items: List<ChecklistItem>,
    onAdd: (String) -> Unit = {},
): ChecklistRepository = object : ChecklistRepository {
    override fun observePending() = flow { emit(items) }
    override fun observeAll() = flow { emit(items) }
    override suspend fun markCompleted(id: String, at: Long) = Unit
    override suspend fun addCustomTask(title: String) { onAdd(title) }
    override suspend fun delete(id: String) = Unit
}
