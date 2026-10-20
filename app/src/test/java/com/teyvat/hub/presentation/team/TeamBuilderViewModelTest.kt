package com.teyvat.hub.presentation.team

import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.service.TeamValidation
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

/** Verifies the builder enforces the team rules before anything is saved. */
@OptIn(ExperimentalCoroutinesApi::class)
class TeamBuilderViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val teamRepository = RecordingTeamRepository()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saving a valid team stores it and clears the selection`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onCharacterToggled(character("a"))
        viewModel.onTeamNameChange("Tim Uji")
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertEquals(1, teamRepository.savedTeams.size)
        assertEquals("Tim Uji", teamRepository.savedTeams.single().name)
        assertEquals(listOf("a"), teamRepository.savedTeams.single().characterIds)

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.selected.isEmpty())
        assertEquals("", state.teamName)
    }

    @Test
    fun `saving without a name is rejected`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertTrue(teamRepository.savedTeams.isEmpty())
    }

    @Test
    fun `saving an empty team is rejected`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onTeamNameChange("Tim Kosong")
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertTrue(teamRepository.savedTeams.isEmpty())
    }

    @Test
    fun `toggling a character twice removes it`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.selected.isEmpty())
    }

    @Test
    fun `a team without a name reports a validation error`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.validation is TeamValidation.Invalid)
    }

    @Test
    fun `synergy notes are produced for the current selection`() = runTest {
        val viewModel = teamBuilderViewModel(teamRepository)
        viewModel.onCharacterToggled(character("a", Element.PYRO))
        viewModel.onCharacterToggled(character("b", Element.HYDRO))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.insights.any { it.headline.contains("Vaporize") })
    }
}
