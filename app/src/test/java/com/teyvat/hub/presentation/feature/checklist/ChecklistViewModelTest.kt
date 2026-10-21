package com.teyvat.hub.presentation.feature.checklist

import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.teyvat.hub.domain.checklist.ChecklistItem
import com.teyvat.hub.domain.checklist.ChecklistResetCalculator
import com.teyvat.hub.domain.checklist.ResetCadence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Verifies the ViewModel exposes tasks divided by cadence with pending flags
 * resolved from the reset window.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ChecklistViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks are grouped by cadence`() = runTest {
        val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))
        val items = listOf(
            checklistItem("a", ResetCadence.DAILY),
            checklistItem("b", ResetCadence.WEEKLY),
            checklistItem("c", ResetCadence.DAILY),
            checklistItem("d", ResetCadence.NONE, isCustom = true),
        )
        val viewModel = viewModelWith(resetCalculator, items = items)

        advanceUntilIdle()
        // Skip initial empty state, get first emitted value
        val state = viewModel.uiState.drop(1).first()

        assertEquals(listOf("a", "c"), state.daily.map { it.id })
        assertEquals(listOf("b"), state.weekly.map { it.id })
        assertEquals(listOf("d"), state.custom.map { it.id })
    }

    @Test
    fun `completed today task is excluded from daily pending list`() = runTest {
        val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))
        // Fixed time: 2026-10-18 12:00 Asia/Shanghai (well after 04:00 reset)
        val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
            clear()
            set(2026, Calendar.OCTOBER, 18, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val items = listOf(
            checklistItem("done", ResetCadence.DAILY, completedAt = now),
            checklistItem("pending", ResetCadence.DAILY, completedAt = null),
        )
        val viewModel = viewModelWith(resetCalculator, items = items, timeProvider = { now })

        advanceUntilIdle()
        val state = viewModel.uiState.drop(1).first()

        assertEquals(listOf("pending"), state.daily.map { it.id })
    }

    @Test
    fun `never completed custom task stays visible`() = runTest {
        val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))
        val items = listOf(checklistItem("todo", ResetCadence.NONE, isCustom = true))
        val viewModel = viewModelWith(resetCalculator, items = items)

        advanceUntilIdle()
        val state = viewModel.uiState.drop(1).first()

        assertTrue(state.custom.isNotEmpty())
    }

    @Test
    fun `blank custom task is rejected`() = runTest {
        val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))
        var added: String? = "unset"
        val viewModel = viewModelWith(resetCalculator, items = emptyList(), onAdd = { title -> added = title })

        viewModel.onAddCustomTask("   ")
        advanceUntilIdle()

        assertEquals("unset", added)
    }

    @Test
    fun `custom task title is trimmed before being added`() = runTest {
        val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))
        var added: String? = null
        val viewModel = viewModelWith(resetCalculator, items = emptyList(), onAdd = { title -> added = title })

        viewModel.onAddCustomTask("  Kumpulkan mora  ")
        advanceUntilIdle()

        assertEquals("Kumpulkan mora", added)
    }

    private fun viewModelWith(
        resetCalculator: ChecklistResetCalculator,
        items: List<ChecklistItem>,
        onAdd: (String) -> Unit = {},
        timeProvider: () -> Long = { System.currentTimeMillis() },
    ): ChecklistViewModel = ChecklistViewModel(
        repository = fakeChecklistRepository(items, onAdd),
        resetCalculator = resetCalculator,
        timeProvider = timeProvider,
    )
}