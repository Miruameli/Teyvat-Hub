package com.teyvat.hub.presentation.checklist

import com.teyvat.hub.application.ChecklistRepository
import com.teyvat.hub.domain.model.ChecklistItem
import com.teyvat.hub.domain.model.ResetCadence
import com.teyvat.hub.domain.service.ChecklistResetCalculator
import java.util.TimeZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
    private val resetCalculator = ChecklistResetCalculator(TimeZone.getTimeZone("Asia/Shanghai"))

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
        val items = listOf(
            item("a", ResetCadence.DAILY),
            item("b", ResetCadence.WEEKLY),
            item("c", ResetCadence.DAILY),
            item("d", ResetCadence.NONE, isCustom = true),
        )
        val viewModel = viewModelWith(items)

        advanceUntilIdle()
        val state = viewModel.uiState.first()

        assertEquals(listOf("a", "c"), state.daily.map { it.id })
        assertEquals(listOf("b"), state.weekly.map { it.id })
        assertEquals(listOf("d"), state.custom.map { it.id })
    }

    @Test
    fun `completed today task is excluded from daily pending list`() = runTest {
        val now = System.currentTimeMillis()
        val items = listOf(
            item("done", ResetCadence.DAILY, completedAt = now),
            item("pending", ResetCadence.DAILY, completedAt = null),
        )
        val viewModel = viewModelWith(items, timeProvider = { now })

        advanceUntilIdle()
        val state = viewModel.uiState.first()

        assertEquals(listOf("pending"), state.daily.map { it.id })
    }

    @Test
    fun `never completed custom task stays visible`() = runTest {
        val items = listOf(item("todo", ResetCadence.NONE, isCustom = true))
        val viewModel = viewModelWith(items)

        advanceUntilIdle()
        val state = viewModel.uiState.first()

        assertTrue(state.custom.isNotEmpty())
    }

    @Test
    fun `blank custom task is rejected`() = runTest {
        var added: String? = "unset"
        val viewModel = viewModelWith(emptyList()) { title -> added = title }

        viewModel.onAddCustomTask("   ")
        advanceUntilIdle()

        assertEquals(null, added)
    }

    @Test
    fun `custom task title is trimmed before being added`() = runTest {
        var added: String? = null
        val viewModel = viewModelWith(emptyList()) { title -> added = title }

        viewModel.onAddCustomTask("  Kumpulkan mora  ")
        advanceUntilIdle()

        assertEquals("Kumpulkan mora", added)
    }

    private fun viewModelWith(
        items: List<ChecklistItem>,
        onAdd: (String) -> Unit = {},
        timeProvider: () -> Long = { System.currentTimeMillis() },
    ): ChecklistViewModel {
        val repository = object : ChecklistRepository {
            override fun observePending() = flow { emit(items) }
            override fun observeAll() = flow { emit(items) }
            override suspend fun markCompleted(id: String, at: Long) = Unit
            override suspend fun addCustomTask(title: String) { onAdd(title) }
            override suspend fun delete(id: String) = Unit
        }
        return ChecklistViewModel(repository, resetCalculator, timeProvider)
    }

    private fun item(
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
}