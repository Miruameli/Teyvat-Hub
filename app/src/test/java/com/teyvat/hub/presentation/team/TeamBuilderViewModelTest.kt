package com.teyvat.hub.presentation.team

import com.teyvat.hub.application.CharacterRepository
import com.teyvat.hub.application.TeamRepository
import com.teyvat.hub.domain.model.Character
import com.teyvat.hub.domain.model.CharacterRole
import com.teyvat.hub.domain.model.Element
import com.teyvat.hub.domain.model.Rarity
import com.teyvat.hub.domain.model.Team
import com.teyvat.hub.domain.model.WeaponType
import com.teyvat.hub.domain.service.TeamSynergyAnalyzer
import com.teyvat.hub.domain.service.TeamValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
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

/** Verifies the builder enforces the team rules before anything is saved. */
@OptIn(ExperimentalCoroutinesApi::class)
class TeamBuilderViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val savedTeams = mutableListOf<Team>()

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
        val viewModel = viewModel()
        viewModel.onCharacterToggled(character("a"))
        viewModel.onTeamNameChange("Tim Uji")
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertEquals(1, savedTeams.size)
        assertEquals("Tim Uji", savedTeams.single().name)
        assertEquals(listOf("a"), savedTeams.single().characterIds)

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.selected.isEmpty())
        assertEquals("", state.teamName)
    }

    @Test
    fun `saving without a name is rejected`() = runTest {
        val viewModel = viewModel()
        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertTrue(savedTeams.isEmpty())
    }

    @Test
    fun `saving an empty team is rejected`() = runTest {
        val viewModel = viewModel()
        viewModel.onTeamNameChange("Tim Kosong")
        advanceUntilIdle()

        viewModel.onSaveTeam()
        advanceUntilIdle()

        assertTrue(savedTeams.isEmpty())
    }

    @Test
    fun `toggling a character twice removes it`() = runTest {
        val viewModel = viewModel()
        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        viewModel.onCharacterToggled(character("a"))
        advanceUntilIdle()

        val state = viewModel.uiState.drop(1).first()
        assertTrue(state.selected.isEmpty())
    }

    @Test
    fun `a fifth character is refused because the party is full`() = runTest {
        val viewModel = viewModel()
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
        val viewModel = viewModel()
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

    private fun viewModel(): TeamBuilderViewModel {
        val teamRepository = object : TeamRepository {
            override fun observeSaved(): Flow<List<Team>> = flow { emit(emptyList()) }
            override fun observeTemplates(): Flow<List<Team>> = flow { emit(emptyList()) }
            override suspend fun save(team: Team) { savedTeams += team }
            override suspend fun delete(id: String) = Unit
        }
        val characterRepository = object : CharacterRepository {
            override fun observeRoster(): Flow<List<Character>> = flow { emit(emptyList()) }
            override fun observeFavorites(): Flow<List<Character>> = flow { emit(emptyList()) }
            override fun search(query: String): Flow<List<Character>> = flow { emit(emptyList()) }
            override suspend fun toggleFavorite(id: String) = Unit
        }
        return TeamBuilderViewModel(
            teamRepository = teamRepository,
            characterRepository = characterRepository,
            validator = TeamValidator(),
            analyzer = TeamSynergyAnalyzer(),
        )
    }

    private fun character(id: String, element: Element = Element.PYRO) = Character(
        id = id,
        name = "Character $id",
        element = element,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = CharacterRole.MAIN_DPS,
    )
}
