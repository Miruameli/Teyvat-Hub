package com.teyvat.hub.presentation.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teyvat.hub.application.ChecklistRepository
import com.teyvat.hub.domain.model.ChecklistItem
import com.teyvat.hub.domain.model.ResetCadence
import com.teyvat.hub.domain.service.ChecklistResetCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Checklist state split by cadence, with pending flags already resolved. */
data class ChecklistUiState(
    val daily: List<ChecklistItem> = emptyList(),
    val weekly: List<ChecklistItem> = emptyList(),
    val custom: List<ChecklistItem> = emptyList(),
)

/**
 * Drives the checklist screen.
 *
 * Pending status is recomputed from the server reset window on every emission,
 * so a task completed yesterday automatically appears pending again after the
 * 04:00 UTC+8 reset without any background job.
 */
@HiltViewModel
class ChecklistViewModel @Inject constructor(
    private val repository: ChecklistRepository,
    private val resetCalculator: ChecklistResetCalculator,
    private val timeProvider: () -> Long = { System.currentTimeMillis() },
) : ViewModel() {

    val uiState: StateFlow<ChecklistUiState> =
        repository.observeAll()
            .map { items -> splitByCadence(items) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChecklistUiState(),
            )

    private fun splitByCadence(items: List<ChecklistItem>): ChecklistUiState {
        val now = timeProvider()
        return ChecklistUiState(
            daily = items.filterPending(ResetCadence.DAILY, now),
            weekly = items.filterPending(ResetCadence.WEEKLY, now),
            custom = items.filter { it.isCustom },
        )
    }

    private fun List<ChecklistItem>.filterPending(
        cadence: ResetCadence,
        now: Long,
    ): List<ChecklistItem> = this
        .filter { it.cadence == cadence }
        .filter { item ->
            item.isPendingAt(now, resetCalculator.windowStart(item, now))
        }

    fun onTaskCompleted(item: ChecklistItem) {
        viewModelScope.launch {
            repository.markCompleted(item.id, timeProvider())
        }
    }

    fun onAddCustomTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addCustomTask(title) }
    }

    fun onDeleteTask(item: ChecklistItem) {
        viewModelScope.launch { repository.delete(item.id) }
    }
}