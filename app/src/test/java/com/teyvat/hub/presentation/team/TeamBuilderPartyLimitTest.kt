package com.teyvat.hub.presentation.team

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
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
 * Verifies the party size limit.
 *
 * A team in Genshin Impact holds exactly four members, so the builder has to
 * refuse a fifth pick instead of letting the selection grow and failing only
 * at save time.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TeamBuilderPartyLimitTest {

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
    fun `a fifth character is refused because the party is full`() = runTest {
        val viewModel = teamBuilderViewModel()
        listOf("a", "b", "c", "d").forEach { viewModel.onCharacterToggled(character(it)) }
        advanceUntilIdle()

        viewModel.onCharacterToggled(character("e"))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertEquals(4, state.selected.size)
        assertTrue(state.selected.none { it.id == "e" })
    }

    @Test
    fun `removing a member frees a slot again`() = runTest {
        val viewModel = teamBuilderViewModel()
        listOf("a", "b", "c", "d").forEach { viewModel.onCharacterToggled(character(it)) }
        advanceUntilIdle()

        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()
        viewModel.onCharacterToggled(character("e"))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertEquals(4, state.selected.size)
        assertTrue(state.selected.any { it.id == "e" })
    }
}
