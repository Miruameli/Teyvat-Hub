package com.teyvat.hub.presentation.feature.roster

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.teyvat.hub.application.repository.CharacterRepository
import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.domain.character.CharacterRole
import com.teyvat.hub.domain.character.Element
import com.teyvat.hub.domain.character.Rarity
import com.teyvat.hub.domain.character.WeaponType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Verifies what the roster screen shows.
 *
 * The three cases cover the ways the list can be narrowed: a search, the
 * favourites filter, and both at once.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RosterViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val roster = (1..22).map { character("char-$it", isFavorite = it <= 3) }
    private val repository = FakeCharacterRepository(roster)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `an empty query shows the whole roster`() = runTest {
        val viewModel = RosterViewModel(repository)

        val state = viewModel.uiState.first { it.characters.isNotEmpty() }

        assertEquals(22, state.characters.size)
    }

    @Test
    fun `a search shows more than ten matches`() = runTest {
        val viewModel = RosterViewModel(repository)

        viewModel.onQueryChange("char")
        val state = viewModel.uiState.first { it.query == "char" && it.characters.isNotEmpty() }

        assertTrue("expected more than ten matches", state.characters.size > 10)
    }

    @Test
    fun `the favourites filter shows only favourites`() = runTest {
        val viewModel = RosterViewModel(repository)

        viewModel.onToggleFavoritesOnly(true)
        val state = viewModel.uiState.first { it.onlyFavorites }

        assertEquals(3, state.characters.size)
        assertTrue(state.characters.all { it.isFavorite })
    }

    @Test
    fun `a search combined with the favourites filter keeps only favourites`() = runTest {
        val viewModel = RosterViewModel(repository)

        viewModel.onToggleFavoritesOnly(true)
        viewModel.onQueryChange("char")
        val state = viewModel.uiState.first {
            it.query == "char" && it.onlyFavorites && it.characters.isNotEmpty()
        }

        assertTrue(state.characters.isNotEmpty())
        assertTrue(state.characters.all { it.isFavorite })
    }

    private fun character(id: String, isFavorite: Boolean) = Character(
        id = id,
        name = id,
        element = Element.PYRO,
        weaponType = WeaponType.SWORD,
        rarity = Rarity.FIVE,
        region = "Mondstadt",
        role = CharacterRole.MAIN_DPS,
        isFavorite = isFavorite,
    )

    /** Repository double that answers a search by name prefix. */
    private class FakeCharacterRepository(
        private val roster: List<Character>,
    ) : CharacterRepository {
        override fun observeRoster(): Flow<List<Character>> = flow { emit(roster) }
        override fun observeFavorites(): Flow<List<Character>> =
            flow { emit(roster.filter { it.isFavorite }) }

        override fun search(query: String, limit: Int): Flow<List<Character>> = flow {
            emit(roster.filter { it.name.contains(query, ignoreCase = true) }.take(limit))
        }

        override suspend fun toggleFavorite(id: String) = Unit
    }
}
